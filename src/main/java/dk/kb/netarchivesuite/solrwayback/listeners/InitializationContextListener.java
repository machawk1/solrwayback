package dk.kb.netarchivesuite.solrwayback.listeners;

import java.lang.reflect.Constructor;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.kb.netarchivesuite.solrwayback.interfaces.ArcFileLocationResolverInterface;
import dk.kb.netarchivesuite.solrwayback.parsers.ArcParserFileResolver;
import dk.kb.netarchivesuite.solrwayback.properties.PropertiesLoader;
import dk.kb.netarchivesuite.solrwayback.properties.PropertiesLoaderWeb;
import dk.kb.netarchivesuite.solrwayback.solr.NetarchiveSolrClient;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class InitializationContextListener implements ServletContextListener {

    private static final Logger log = LoggerFactory.getLogger(InitializationContextListener.class);
    private static String version;
    
    // this is called by the web-container before opening up for requests.(defined in web.xml)
    public void contextInitialized(ServletContextEvent event) {

        log.info("solrwayback starting up...");
        Properties props = new Properties();
        try {
          
          
          
          
            String webbAppContext = event.getServletContext().getContextPath();                    
            props.load(InitializationContextListener.class.getResourceAsStream("/build.properties"));
            version = props.getProperty("APPLICATION.VERSION");
            PropertiesLoader.initProperties(webbAppContext+".properties"); //backend. If contextroot is not solrwayback, it will first look for that context specific propertyfile                                  
            PropertiesLoaderWeb.initProperties(webbAppContext+"web.properties"); //frontend
            PropertiesLoaderWeb.SOLRWAYBACK_VERSION = version;
            
            // initialise the solrclient
            NetarchiveSolrClient.initialize(PropertiesLoader.SOLR_SERVER);
            
            //Load the warcfilelocation resolver.                        
            String arcFileResolverClass = PropertiesLoader.WARC_FILE_RESOLVER_CLASS;
            if (arcFileResolverClass != null){            
            Class c = Class.forName(arcFileResolverClass);                               
            Constructor constructor = c.getConstructor(); //Default constructor, no arguments
            ArcFileLocationResolverInterface resolverImpl= (ArcFileLocationResolverInterface) constructor.newInstance();          
            ArcParserFileResolver.setArcFileLocationResolver(resolverImpl); //Set this on the Facade
            log.info("Using warc-file-resolver implementation class:"+arcFileResolverClass);
            }
            else{
              log.info("Using default warc-file-resolver implementation");
            }
            
            
            //TODO Delete code later. this is just a backup implementation 
            /* This works with socks 5 
            new Thread(new Runnable() {
              public void run() {
                SOCKS.main(new String[]{"/home/teg/workspace/solrwayback/socks.properties"});
              }
             }).start();
            */
                      
                       
            log.info("solrwayback version " + version + " started successfully");

        } catch (Exception e) {
            log.error("failed to initialize service", e);
            e.printStackTrace();
            throw new RuntimeException("failed to initialize service", e);
        }
    }

    // this is called by the web-container at shutdown. (defined in web.xml)
    public void contextDestroyed(ServletContextEvent sce) {
        try {        
          log.info("solrwayback shutting down...");
        } catch (Exception e) {
            log.error("failed to shutdown solrwayback", e);
        }

    }

}
