### MDB
JEE timout configuration with MDB, try to use both `LongRunningService`and `LongRunningServiceWrongTry`

```java
@MessageDriven(name = "TestMDB", activationConfig = {
@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
@ActivationConfigProperty(propertyName = "destination", propertyValue = "testQueue"),
@ActivationConfigProperty(propertyName = "transactionTimeout", propertyValue="4")
})
```

###