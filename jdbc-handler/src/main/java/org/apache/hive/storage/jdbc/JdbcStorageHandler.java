package org.apache.hive.storage.jdbc;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hive.conf.Constants;
import org.apache.hadoop.hive.metastore.HiveMetaHook;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.metadata.HiveStorageHandler;
import org.apache.hadoop.hive.ql.metadata.JarUtils;
import org.apache.hadoop.hive.ql.plan.TableDesc;
import org.apache.hadoop.hive.ql.security.authorization.HiveAuthorizationProvider;
import org.apache.hadoop.hive.serde2.AbstractSerDe;
import org.apache.hadoop.mapred.InputFormat;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.OutputFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.hive.storage.jdbc.conf.JdbcStorageConfigManager;
import java.io.IOException;
import java.lang.IllegalArgumentException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
public class JdbcStorageHandler implements HiveStorageHandler {
  private static Logger LOGGER=LoggerFactory.getLogger(JdbcStorageHandler.class);
  private Configuration conf;
  @Override public void setConf(  Configuration conf){
    this.conf=conf;
  }
  @Override public Configuration getConf(){
    return this.conf;
  }
  @SuppressWarnings("rawtypes") @Override public Class<? extends InputFormat> getInputFormatClass(){
    return JdbcInputFormat.class;
  }
  @SuppressWarnings("rawtypes") @Override public Class<? extends OutputFormat> getOutputFormatClass(){
    return JdbcOutputFormat.class;
  }
  @Override public Class<? extends AbstractSerDe> getSerDeClass(){
    return JdbcSerDe.class;
  }
  @Override public HiveMetaHook getMetaHook(){
    return null;
  }
  @Override public void configureInputJobProperties(  TableDesc tableDesc,  Map<String,String> jobProperties){
    try {
      LOGGER.debug("Adding properties to input job conf");
      Properties properties=tableDesc.getProperties();
      JdbcStorageConfigManager.copyConfigurationToJob(properties,jobProperties);
    }
 catch (    Exception e) {
      throw new IllegalArgumentException(e);
    }
  }
  @Override public void configureInputJobCredentials(  TableDesc tableDesc,  Map<String,String> jobSecrets){
    try {
      LOGGER.debug("Adding secrets to input job conf");
      Properties properties=tableDesc.getProperties();
      JdbcStorageConfigManager.copySecretsToJob(properties,jobSecrets);
    }
 catch (    Exception e) {
      throw new IllegalArgumentException(e);
    }
  }
  @Override public void configureTableJobProperties(  TableDesc tableDesc,  Map<String,String> jobProperties){
    try {
      LOGGER.debug("Adding properties to input job conf");
      Properties properties=tableDesc.getProperties();
      JdbcStorageConfigManager.copyConfigurationToJob(properties,jobProperties);
    }
 catch (    Exception e) {
      throw new IllegalArgumentException(e);
    }
  }
  @Override public void configureOutputJobProperties(  TableDesc tableDesc,  Map<String,String> jobProperties){
  }
  @Override public HiveAuthorizationProvider getAuthorizationProvider() throws HiveException {
    return null;
  }
  @Override public void configureJobConf(  TableDesc tableDesc,  JobConf jobConf){
    List<Class<?>> classesToLoad=new ArrayList<>();
    classesToLoad.add(org.apache.hive.storage.jdbc.JdbcInputSplit.class);
    classesToLoad.add(org.apache.commons.dbcp2.BasicDataSourceFactory.class);
    classesToLoad.add(org.apache.commons.pool2.impl.GenericObjectPool.class);
    try {
      classesToLoad.add(Class.forName("com.mysql.jdbc.Driver"));
    }
 catch (    Exception e) {
    }
    try {
      classesToLoad.add(Class.forName("org.postgresql.Driver"));
    }
 catch (    Exception e) {
    }
    try {
      classesToLoad.add(Class.forName("oracle.jdbc.OracleDriver"));
    }
 catch (    Exception e) {
    }
    try {
      classesToLoad.add(Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver"));
    }
 catch (    Exception e) {
    }
    try {
      classesToLoad.add(Class.forName("com.ibm.db2.jcc.DB2Driver"));
    }
 catch (    Exception e) {
    }
    try {
      JarUtils.addDependencyJars(conf,classesToLoad);
    }
 catch (    IOException e) {
      LOGGER.error("Could not add necessary JDBC storage handler dependencies to classpath",e);
    }
  }
  @Override public String toString(){
    return Constants.JDBC_HIVE_STORAGE_HANDLER_ID;
  }
  public NettyChannelHandler(){
  }
}
