Sizzle
======

A Single class java dependency management library. No annotations or xml.

### Download jar
https://github.com/rizvn/sizzle/blob/master/sizzle-1.3.jar

### Quick Start:

    //create a new context no properties
    Sizzle sizzle = new Sizzle()

    //--   or  with properties file from relative path ---
    Sizzle sizzle = new Sizzle("application.properties");


    //create a singleton for instance using the default constructor
    ProductDao productDao = sizzle.getOrCreate(ProductDao.class)
    
If a properties file is provided. Sizzle will load the properties. The properties object can be retrieved using <code><b>sizzle.get("ContextProperties")</b></code> which returns a java.util.Properties instance loaded with the properties

<strong>ProductDao.class </strong> does not have any special annotations it is normal class which implements <b>SizzleAware</b>. So when it is created the setSizzle(..) method is called passing in the sizzle instance that created it. This can be stored in an instance varaible for later use.  <b>Instantiation is thread safe </b> so we can lazily create beans when needed.  Below is the code for ProductDao:

    public class ProductDao implements Sizzle.SizzleAware{
       Sizzle sizzle;
  
       @Override
       public void setSizzle(Sizzle sizzle) {
           this.sizzle = sizzle;
       }
  
       List<String> getProducts(){
         ...
       }
    }
    
Here is another class called ProductController:

    public class ProductController implements Sizzle.SizzleAware{
       Sizzle sizzle;
  
       @Override
       public void setSizzle(Sizzle sizzle) {
           this.sizzle = sizzle;
       }
        
    }


### Getting a singleton
If want to use the ProductDao in the ProductController we can just fetch ProductDao using sizzle. for example:
    public class ProductController implements SizzleAware{
       Sizzle sizzle;
  
       @Override
       public void setSizzle(Sizzle sizzle) {
           this.sizzle = sizzle;
       }
  
       List<String> getProducts(){
          ProductDao productDao = sizzle.getOrCreate(ProductDao.class);
          return productDao.getProductNames();
       }
    }

### Manually Registering by Class Type and Name
    Sizzle sizzle = new Sizzle()
    
    //freemarker configuration
    Configuration conf = new Configuration(Configuration.VERSION_2_3_21);
    conf.setClassForTemplateLoading(new Object().getClass(), "/templates");
    sizzle.addSingletonBean(Configuration.class, conf, "freemarker");

To retrieve this conf instance anywhere in code, call
 <code> sizzle.get(Configuation.class)</code> <b>or by name</b><code>sizzle.get("freemarker") </code>


### Manually Registering by Class Only
    Sizzle sizzle = new Sizzle()
    
    //datasource
    Properties props = ctx.get("ContextProperties");
    HikariDataSource ds = new HikariDataSource();
    ds.setDriverClassName(props.getProperty("db.driver"));
    ds.setUsername(props.getProperty("db.user"));
    ds.setPassword(props.getProperty("db.password"));
    ds.setJdbcUrl(props.getProperty("db.url"));
    sizzle.addSingletonBean(DataSource.class, ds);

To retrieve this datasource instance anywhere in code call
 <code>sizzle.get(DataSource.class)</code>

### Manually Registering by Name Only
    Sizzle sizzle = new Sizzle() 
    //datasource
    Properties props = ctx.get("ContextProperties");
    HikariDataSource ds = new HikariDataSource();
    ds.setDriverClassName(props.getProperty("db.driver"));
    ds.setUsername(props.getProperty("db.user"));
    ds.setPassword(props.getProperty("db.password"));
    ds.setJdbcUrl(props.getProperty("db.url"));
    sizzle.addSingletonBean("TheDb", ds);

To retrieve this datasource instance anywhere in code call
 <code> sizzle.get("TheDb")</code>

### Manually Registering by Instance Only
    Sizzle sizzle = new Sizzle() 
    //datasource
    Properties props = Sizzle.getCtx().get("ContextProperties");
    HikariDataSource ds = new HikariDataSource();
    ds.setDriverClassName(props.getProperty("db.driver"));
    ds.setUsername(props.getProperty("db.user"));
    ds.setPassword(props.getProperty("db.password"));
    ds.setJdbcUrl(props.getProperty("db.url"));
    sizzle.addSingletonBean(ds);

To retrieve this HikariDataSource instance anywhere in code call
 <code>sizzle.get(HikariDataSource.class)</code> since that is the actual class of the instance

