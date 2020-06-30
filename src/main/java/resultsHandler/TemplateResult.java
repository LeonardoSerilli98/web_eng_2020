package resultsHandler;

import data.DataException;
import freemarker.core.HTMLOutputFormat;
import freemarker.core.JSONOutputFormat;
import freemarker.core.XMLOutputFormat;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapperBuilder;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.Canale;
import models.Fascia;
import models.Genere;
import resources.Database;

/**
 *
 * @author Giuseppe Della Penna
 */
public class TemplateResult {

    protected ServletContext context;
    protected Configuration cfg;
    protected Database db;


    public TemplateResult(ServletContext context) {
        this.context = context;
        init();
    }

    //metodo di configurazione 
    private void init() {
        db = new Database();
        
        cfg = new Configuration(Configuration.VERSION_2_3_26);
        cfg.setOutputEncoding("ISO-8859-1");
        cfg.setDefaultEncoding("ISO-8859-1");
        cfg.setServletContextForTemplateLoading(context, "templates");
        
        if (context.getInitParameter("view.date_format") != null) {
            cfg.setDateTimeFormat(context.getInitParameter("view.date_format"));
        }

        //l'owb serve a trasformare oggetti in strutture manipolabli dal template
        DefaultObjectWrapperBuilder owb = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_26);
        cfg.setObjectWrapper(owb.build());
        // il set dell'output format serve per sanitizzare l'html
        cfg.setOutputFormat(HTMLOutputFormat.INSTANCE);
    }

    //restituisce un datamodel (mappa) di base per il template di outline
    protected Map getDefaultDataModel() throws DataException {
            //aggiungiamo al datamodel default il template di outline
            Map default_data_model = new HashMap();
            default_data_model.put("outline_tpl", context.getInitParameter("view.outline_template"));
            //poi aggiungiamo anche una map con i dati da usare nell'outline
            Map init_tpl_data = new HashMap();
            //qui possiamo agginger tutti i dati da passare all' outline
            default_data_model.put("defaults", init_tpl_data);
                    
            List<Genere> generi = (db.getDatalayer()).getGenereDAO().getAll();
            List<Canale> canali = (db.getDatalayer()).getCanaleDAO().getAll();
            List<Fascia> fasce = (db.getDatalayer()).getFasciaDAO().getAll();

            default_data_model.put("generi", generi);
            default_data_model.put("canali", canali);
            default_data_model.put("fasce", fasce);
            
            return default_data_model;
    }

    //restituisce un data model estratto dagli attributi della request
    protected Map getRequestDataModel(HttpServletRequest request) {
        Map datamodel = new HashMap();
        Enumeration attrs = request.getAttributeNames();
        while (attrs.hasMoreElements()) {
            String attrname = (String) attrs.nextElement();
            datamodel.put(attrname, request.getAttribute(attrname));
        }
        return datamodel;
    }

    //di base scarica il template di outline e, se specificato un template specifico,
    // lo aggiunge nel datamodel da passare all'outline cosi che questo potra aggiungerlo al suo interno
    protected void process(String tplname, Map datamodel, Writer out) throws TemplateManagerException, DataException {
        Template t;
        
        Map<String, Object> localdatamodel = getDefaultDataModel();
        if (datamodel != null) {
            //mettiamo nel default datamodel tutti i dati al datamodel locale
            // che sarà quello effettiavamente usato dal template
            localdatamodel.putAll(datamodel);
        }
        
        String outline_name = (String) localdatamodel.get("outline_tpl");
        try {
            if (outline_name == null || outline_name.isEmpty()) {
                //se non c'è un outline, carichiamo semplicemente il template specificato
                t = cfg.getTemplate(tplname);
            } else {
                //un template di outline è stato specificato:
                // quind carichiamo sempre quello passandogli nel datamodel...
                t = cfg.getTemplate(outline_name);
                //... il template specifico tramite content_tpl
                localdatamodel.put("content_tpl", tplname);

                //si suppone che l'outline includa questo secondo template
            }
            //associamo i dati al template e lo mandiamo in output
            t.process(localdatamodel, out);
        } catch (IOException | TemplateException e) {
            throw new TemplateManagerException("Template error: " + e.getMessage(), e);
        }
    }

    //questa versione di activate accetta un modello dati esplicito
    public void activate(String tplname, Map datamodel, HttpServletResponse response) throws TemplateManagerException {
        //impostiamo il content type, se specificato dall'utente, o usiamo il default
        //set the output content type, if user-specified, or use the default
        String contentType = (String) datamodel.get("contentType");
        if (contentType == null) {
            contentType = "text/html";
        }
        response.setContentType(contentType);

        //impostiamo il tipo di output: in questo modo freemarker abiliterà il necessario escaping
        switch (contentType) {
            case "text/html":
                cfg.setOutputFormat(HTMLOutputFormat.INSTANCE);
                break;
            case "text/xml":
            case "application/xml":
                cfg.setOutputFormat(XMLOutputFormat.INSTANCE);
                break;
            case "application/json":
                cfg.setOutputFormat(JSONOutputFormat.INSTANCE);
                break;
            default:
                break;
        }

        //impostiamo l'encoding, se specificato dall'utente, o usiamo il default
        String encoding = (String) datamodel.get("encoding");
        if (encoding == null) {
            encoding = cfg.getOutputEncoding();
        }
        response.setCharacterEncoding(encoding);

        try {
            process(tplname, datamodel, response.getWriter());
        } catch (IOException ex) {
            throw new TemplateManagerException("Template error: " + ex.getMessage(), ex);
        } catch (DataException ex) {
            throw new TemplateManagerException("Data error: " + ex.getMessage(), ex);
        }
    }

    //questa versione di activate estrae un modello dati dagli attributi della request
    public void activate(String tplname, HttpServletRequest request, HttpServletResponse response) throws TemplateManagerException {
        Map datamodel = getRequestDataModel(request);
        activate(tplname, datamodel, response);
    }

    
    /*
    //questa versione di activate può essere usata per generare output non diretto verso il browser, ad esempio
    //su un file
    public void activate(String tplname, Map datamodel, OutputStream out) throws TemplateManagerException {
        //impostiamo l'encoding, se specificato dall'utente, o usiamo il default
        String encoding = (String) datamodel.get("encoding");
        if (encoding == null) {
            encoding = cfg.getOutputEncoding();
        }
        try {
            //notare la gestione dell'encoding, che viene invece eseguita implicitamente tramite il setContentType nel contesto servlet
            process(tplname, datamodel, new OutputStreamWriter(out, encoding));
        } catch (UnsupportedEncodingException ex) {
            throw new TemplateManagerException("Template error: " + ex.getMessage(), ex);
        }
    }
    */
}
