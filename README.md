Sizzle
======

A lightweight java dependency injection framework. No annotations or xml

### Quick Start:

    //create a new context no properties
    Sizzle ctx = Sizzle.newCtx(null);
    
    //--   or  with properties file from relative path ---
    Sizzle ctx = Sizzle.newCtx("application.properties");
     
    
    //create a singleton for instance using the default constructor 
    ProductDao productDao = ctx.getOrCreate(ProductDao.class)

<strong>ProductDao.class </strong> does not have any special annotations. Sizzle creates an instance of ProductDao using its default constructor if the bean does not exist, otherwise it will return the instance created earlier. <b>Instantiation is thread safe </b> so we can lazily create beans when needed.  Below is a the code for ProductDao. It is just a normal java class. 

    public class ProductDao {
       List<String> getProductNames(){
         ...
       }
    }

### Injecting
Once created the <b><code>Sizzle.getCtx()</code></b> can be called from anywhere in the code to get the context, it will always return the context created when newCtx() was called.

    public class SomeClass{
      public void someMethod() {
        ProductDao productDao = Sizzle.getCtx().getOrCreate(ProductDao.class);
        List<String> names = productDao.getProductNames();
      }
    }

### Manually Registering by Class Type and Name
    //freemarker configuration
    Configuration conf = new Configuration(Configuration.VERSION_2_3_21);
    conf.setClassForTemplateLoading(new Object().getClass(), "/templates");
    ctx.addSingletonBean(Configuration.class, conf, "freemarker");
 
To retrieve this conf instance anywhere in code, call
 <code> Sizzle.getCtx().get(Configuation.class)</code> <b>or</b> <code>Sizzle.getCtx().get("freemarker") </code>
     

### Manually Registering by Class Only
    //datasource
    Properties props = ctx.get("ContextProperties");
    HikariDataSource ds = new HikariDataSource();
    ds.setDriverClassName(props.getProperty("db.driver"));
    ds.setUsername(props.getProperty("db.user"));
    ds.setPassword(props.getProperty("db.password"));
    ds.setJdbcUrl(props.getProperty("db.url"));
    Sizzle.getCtx().addSingletonBean(DataSource.class, ds);
    
To retrieve this datasource instance anywhere in code call
 <code> Sizzle.getCtx().get(DataSource.class)</code> 

### Manually Registering by Name Only
    //datasource
    Properties props = ctx.get("ContextProperties");
    HikariDataSource ds = new HikariDataSource();
    ds.setDriverClassName(props.getProperty("db.driver"));
    ds.setUsername(props.getProperty("db.user"));
    ds.setPassword(props.getProperty("db.password"));
    ds.setJdbcUrl(props.getProperty("db.url"));
    Sizzle.getCtx().addSingletonBean("TheDb", ds);
    
To retrieve this datasource instance anywhere in code call
 <code> Sizzle.getCtx().get("TheDb")</code> 

### Manually Registering by Instance Only
    //datasource
    Properties props = Sizzle.getCtx().get("ContextProperties");
    HikariDataSource ds = new HikariDataSource();
    ds.setDriverClassName(props.getProperty("db.driver"));
    ds.setUsername(props.getProperty("db.user"));
    ds.setPassword(props.getProperty("db.password"));
    ds.setJdbcUrl(props.getProperty("db.url"));
    Sizzle.getCtx().addSingletonBean(ds);
    
To retrieve this HikariDataSource instance anywhere in code call
 <code> Sizzle.getCtx().get(HikariDataSource.class)</code> since that is the actual class of the instance

