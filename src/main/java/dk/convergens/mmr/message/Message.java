package dk.convergens.mmr.message;

import dk.convergens.mmr.message.log.ErrorLog;
import dk.convergens.mmr.message.log.Log;
import dk.convergens.mmr.message.data.Data;
import dk.convergens.mmr.message.data.MetaData;
import dk.convergens.mmr.message.route.Route;
import dk.convergens.mmr.message.route.RoutingSlip;
import dk.convergens.mmr.message.database.MetaDataFileAttachment;
import dk.convergens.mmr.message.database.AuthorizationCallback;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import io.quarkus.runtime.annotations.RegisterForReflection;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

/**
 *
 * @author Magnus
 */
@RegisterForReflection
public class Message {

    private String uuid;
    private String producerReference;
    private Long exitTime, entryTime;
    private Data data;
    private MetaData metaData;
    private ArrayList<Log> logs = new ArrayList<>();
    private RoutingSlip routingSlip;
    private ErrorLog errorLog;
    private AuthorizationCallback authorizationCallback;
    private MetaDataFileAttachment metaDataAttachment;
    private transient Date date = new Date();
    private transient Log currentLog;
    private transient Jsonb jsonb = JsonbBuilder.create();
    private transient Properties config = new Properties();

    public Message() {

    }

    public MetaDataFileAttachment getMetaDataAttachment() {
        return metaDataAttachment;
    }

    public void setMetaDataAttachment(MetaDataFileAttachment metaDataAttachment) {
        this.metaDataAttachment = metaDataAttachment;
    }

    public AuthorizationCallback getAuthorizationCallback() {
        return authorizationCallback;
    }

    public void setAuthorizationCallback(AuthorizationCallback authorizationCallback) {
        this.authorizationCallback = authorizationCallback;
    }

    public RoutingSlip getRoutingSlip() {
        return routingSlip;
    }

    public void setRoutingSlip(RoutingSlip routingSlip) {
        this.routingSlip = routingSlip;
    }

    /**
     * Starts the log with the name of the currently used module. Set the
     * current date, and the unix time.
     *
     * @param moduleName The name of the currently used module
     */
    public void startLog(String moduleName) {
        currentLog = new Log(moduleName, date.toString(), System.currentTimeMillis(), null, null);
    }

    /**
     * Sets the end unix time. And calls upon the method that sets the total
     * time spend in module. Adding log to the log ArrayList.
     */
    public void endLog() {
        currentLog.setEndTime(System.currentTimeMillis());
        currentLog.setTotalTime();
        logs.add(currentLog);
    }

    public Long getExitTime() {
        return exitTime;
    }

    public void setExitTime(Long exitTime) {
        this.exitTime = exitTime;
    }

    public Long getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(Long entryTime) {
        this.entryTime = entryTime;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getProducerReference() {
        return producerReference;
    }

    public void setProducerReference(String producerReference) {
        this.producerReference = producerReference;
    }

    public ArrayList<Log> getLogs() {
        return logs;
    }

    public MetaData getMetaData() {
        return metaData;
    }

    public void setMetaData(MetaData metaData) {
        this.metaData = metaData;
    }

    /**
     * Calls routingSlip validation, find the highest priority route, with true
     * conditions. Check any valid route was returned. Opens a producer, that
     * will be given the routes topic.
     *
     * @throws Exception
     */
    public void sendToKafkaQue() throws Exception {
        config.put("bootstrap.servers", "cis-x.convergens.dk:9092");
        config.put("retries", 0);
        config.put("acks", "all");
        config.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        config.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        Route route = routingSlip.validateRoutingSlip(metaData);
        if (route != null) {
            endLog();
            Producer<String, String> producer = new KafkaProducer<String, String>(config);
            producer.send(new ProducerRecord<String, String>(route.getTopic(), jsonb.toJson(this)), new Callback() {
                @Override
                public void onCompletion(RecordMetadata rm, Exception excptn) {
                    if (excptn != null) {
                        System.out.println("-------------onFailedQue------------------");
                    }
                }
            });
            producer.close();
        } else if (routingSlip.getRoutes().isEmpty()) {
            endLog();
            sendToKafkaExitQue();
        } else {
            endLog();
            sendToKafkaInvalidQue();
        }
    }

    private void sendToKafkaErrorQue() {
        config.put("bootstrap.servers", "cis-x.convergens.dk:9092");
        config.put("retries", 0);
        config.put("acks", "all");
        config.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        config.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        endLog();
        Producer<String, String> producer = new KafkaProducer<String, String>(config);
        producer.send(new ProducerRecord<String, String>("error", jsonb.toJson(this)), new Callback() {
            @Override
            public void onCompletion(RecordMetadata rm, Exception excptn) {
                if (excptn != null) {
                    System.out.println("-------------onFailedErrorQue------------------");
                }
            }
        });
        producer.close();
    }

    private void sendToKafkaInvalidQue() {
        config.put("bootstrap.servers", "cis-x.convergens.dk:9092");
        config.put("retries", 0);
        config.put("acks", "all");
        config.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        config.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        endLog();
        Producer<String, String> producer = new KafkaProducer<String, String>(config);
        producer.send(new ProducerRecord<String, String>("no-valid-routes", jsonb.toJson(this)), new Callback() {
            @Override
            public void onCompletion(RecordMetadata rm, Exception excptn) {
                if (excptn != null) {
                    System.out.println("-------------onFailedErrorQue------------------");
                }
            }
        });
        producer.close();
    }

    /**
     * Send the Json directly to the end que.
     */
    public void sendToKafkaExitQue() {
        config.put("bootstrap.servers", "cis-x.convergens.dk:9092");
        config.put("retries", 0);
        config.put("acks", "all");
        config.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        config.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        endLog();
        Producer<String, String> producer = new KafkaProducer<String, String>(config);
        producer.send(new ProducerRecord<String, String>("exit", jsonb.toJson(this)), new Callback() {
            @Override
            public void onCompletion(RecordMetadata rm, Exception excptn) {
                if (excptn != null) {
                    System.out.println("-------------onFailedErrorQue------------------");
                }
            }
        });
        producer.close();
    }

    /**
     * Add the thrown exception, add it to the errorLog for analyzation. Calls
     * upon kafka que handling.
     *
     * @param e Takes any exception
     */
    public void handleError(Exception e) {
        errorLog.setStackTrace(e);
        sendToKafkaErrorQue();
    }

    public Message populateMessage(String content) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectReader objectReader = objectMapper.readerForUpdating(this);
        Message msg = objectReader.readValue(content);
        return msg;
    }
}
