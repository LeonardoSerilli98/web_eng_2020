/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import data.DataException;
import data.GuidaTV_DataLayer;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import models.Canale;
import models.Canale_Imp;
import models.Episodio;
import models.Episodio_Imp;
import models.Genere;
import models.Genere_Imp;
import models.Immagine;
import models.Immagine_Imp;
import models.Palinsesto;
import models.Palinsesto_Imp;
import models.Programma;
import models.Programma_Imp;
import models.Stagione;
import models.Stagione_Imp;
import models.Utente;
import resultsHandler.FailureResult;
import resultsHandler.TemplateManagerException;
import resultsHandler.TemplateResult;
import utilities.SecurityLayer;

/**
 *
 * @author leonardo
 */
public class BackEnd_Controller extends Base_Controller {

    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        } else {
            (new FailureResult(getServletContext())).activate((String) request.getAttribute("message"), request, response);
        }
    }

    private void action_default(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, TemplateManagerException, DataException {

        TemplateResult res = new TemplateResult(getServletContext());
        if (request.getAttribute("admin") != null) {

            List<Programma> programmi = ((GuidaTV_DataLayer) request.getAttribute("datalayer")).getProgrammaDAO().getAll();
            request.setAttribute("programmi", programmi);
            res.activate("admin.html", request, response);
        } else {
            request.setAttribute("error", "Devi essere admin");
            res.activate("error.html", request, response);
        }

    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {

        try {

            if (SecurityLayer.checkSession(request) != null) {
                Utente u = ((GuidaTV_DataLayer) request.getAttribute("datalayer")).getUtenteDAO().getUtenteByUsername((String) request.getSession().getAttribute("username"));
                if (u != null) {
                    if (((GuidaTV_DataLayer) request.getAttribute("datalayer")).getUtenteDAO().isAdmin(u.getEmail())) {
                        request.setAttribute("admin", u);

                        if (request.getParameter("form") != null) {
                            switch (request.getParameter("form")) {
                                case "palinsesto":
                                    action_crudPalinsesto(request, response);
                                    break;
                                case "canale":
                                    action_crudCanale(request, response);
                                    break;
                                case "programma":
                                    action_crudProgramma(request, response);
                                    break;
                                case "stagione":
                                    action_crudStagione(request, response);
                                    break;
                                case "episodio":
                                    action_crudEpisodio(request, response);

                                    break;
                                case "genere":
                                    action_crudGenere(request, response);
                                    break;
                            }

                        }
                    }
                }

            }

            action_default(request, response);

        } catch (DataException | TemplateManagerException | IOException ex) {
            Logger.getLogger(BackEnd_Controller.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

    private boolean action_crudPalinsesto(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, DataException {
        Palinsesto p = new Palinsesto_Imp();

        if ((request.getParameter("data").equals("")) || (request.getParameter("oraInizio").equals("")) || (request.getParameter("oraFine").equals(""))) {
            return false;
        }

        System.out.println(Time.valueOf(request.getParameter("oraInizio") + ":00"));
        p.setInizio(Time.valueOf(request.getParameter("oraInizio") + ":00"));
        p.setFine(Time.valueOf(request.getParameter("oraFine") + ":00"));
        p.setData(Date.valueOf(request.getParameter("data")));

        if (request.getParameter("fascia") != null) {
            if (SecurityLayer.checkNumeric(request.getParameter("fascia")) != 0) {
                p.setFascia(((GuidaTV_DataLayer) request.getAttribute("datalayer")).getFasciaDAO().read(SecurityLayer.checkNumeric(request.getParameter("fascia"))));
            } else {
                return false;
            }

        } else {
            return false;
        }
        if (request.getParameter("canale") != null) {
            if (SecurityLayer.checkNumeric(request.getParameter("canale")) != 0) {
                p.setCanale(((GuidaTV_DataLayer) request.getAttribute("datalayer")).getCanaleDAO().read(SecurityLayer.checkNumeric(request.getParameter("canale"))));
            } else {
                return false;
            }
        } else {
            return false;
        }

        if (SecurityLayer.checkNumeric(request.getParameter("programma")) != 0) {
            Programma existProg = ((GuidaTV_DataLayer) request.getAttribute("datalayer")).getProgrammaDAO().read(SecurityLayer.checkNumeric(request.getParameter("programma")));
            if (existProg != null) {
                p.setProgramma(existProg);
                if (existProg.getIsSerie()) {
                    int numStagione = SecurityLayer.checkNumeric(request.getParameter("numeroStagione"));
                    int numEpisodio = SecurityLayer.checkNumeric(request.getParameter("numeroEpisodio"));
                    Episodio episodio = ((GuidaTV_DataLayer) request.getAttribute("datalayer")).getEpisodioDAO().checkCorrectness(existProg.getKey(), numStagione, numEpisodio);
                    System.out.println("check episodio");
                    if (episodio != null) {
                        p.setEpisodio(episodio);
                    }
                }

                List<Palinsesto> pSameDateAndChannel = ((GuidaTV_DataLayer) request.getAttribute("datalayer")).getPalinsestoDAO().getPalinsestiByCanale(p.getCanale(), request.getParameter("data"));
                boolean collision = false;
                for (Palinsesto iterator : pSameDateAndChannel) {
                    if (!(p.getInizio().getTime() > iterator.getFine().getTime() || p.getFine().getTime() < p.getInizio().getTime())) {
                        collision = true;
                    }

                }

                System.out.println("collisio sadafasdgafd" + collision);
                if (collision) {
                    return false;
                }

                Palinsesto existPal = ((GuidaTV_DataLayer) request.getAttribute("datalayer")).getPalinsestoDAO().checkExistence(p);
                if (existPal == null) {
                    ((GuidaTV_DataLayer) request.getAttribute("datalayer")).getPalinsestoDAO().create(p);
                } else {
                    p.setKey(existPal.getKey());
                    ((GuidaTV_DataLayer) request.getAttribute("datalayer")).getPalinsestoDAO().update(p);

                }

                return true;

            }
        }

        return false;
    }

    private boolean action_crudCanale(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, DataException {

        if (request.getParameter("nome").equals("")) {
            return false;
        }

        Canale c = new Canale_Imp();
        Immagine i = img_fromRequest(request, response);

        c.setNome(request.getParameter("nome"));
        c.setImmagine(i);

        Canale existCanale = ((GuidaTV_DataLayer) request.getAttribute("datalayer")).getCanaleDAO().checkExistence(c.getNome());
        if (existCanale != null) {
            c.setKey(existCanale.getKey());
            ((GuidaTV_DataLayer) request.getAttribute("datalayer")).getCanaleDAO().update(c);
        } else {
            ((GuidaTV_DataLayer) request.getAttribute("datalayer")).getCanaleDAO().create(c);
        }

        return true;
    }

    private boolean action_crudProgramma(HttpServletRequest request, HttpServletResponse response) throws DataException, IOException, ServletException {

        if (request.getParameter("nome").equals("")) {
            return false;
        }

        Programma p = new Programma_Imp();
        p.setNome(request.getParameter("nome"));
        p.setApprofondimento(request.getParameter("approfondimento"));
        p.setDescrizione(request.getParameter("descrizione"));

        if (request.getParameter("isSerie") == null) {
            p.setIsSerie(false);
        } else if (SecurityLayer.checkNumeric(request.getParameter("isSerie")) == 1) {
            p.setIsSerie(true);
        }

        if (request.getParameter("genere") != null) {
            if (SecurityLayer.checkNumeric(request.getParameter("genere")) != 0) {
                p.setGenere(((GuidaTV_DataLayer) request.getAttribute("datalayer")).getGenereDAO().read((SecurityLayer.checkNumeric(request.getParameter("genere")))));
            }
        } else {
            return false;
        }

        Immagine i = img_fromRequest(request, response);
        p.setImmagine(i);
        Programma existProg = ((GuidaTV_DataLayer) request.getAttribute("datalayer")).getProgrammaDAO().checkExistence(p.getNome());

        if (existProg != null) {
            p.setKey(existProg.getKey());
            ((GuidaTV_DataLayer) request.getAttribute("datalayer")).getProgrammaDAO().update(p);

        } else {
            ((GuidaTV_DataLayer) request.getAttribute("datalayer")).getProgrammaDAO().create(p);

        }

        return true;

    }

    private boolean action_crudStagione(HttpServletRequest request, HttpServletResponse response) throws DataException, IOException, ServletException {
        int num = SecurityLayer.checkNumeric(request.getParameter("numero"));
        int programmaID = SecurityLayer.checkNumeric(request.getParameter("programma"));

        if (num == 0 || programmaID == 0) {
            return false;
        }

        Stagione s = new Stagione_Imp();
        s.setNumero(num);
        s.setProgramma(((GuidaTV_DataLayer) request.getAttribute("datalayer")).getProgrammaDAO().read(programmaID));
        Immagine i = img_fromRequest(request, response);
        s.setImmagine(i);

        Stagione exist = ((GuidaTV_DataLayer) request.getAttribute("datalayer")).getStagioneDAO().getStagioneByProgramma(s.getProgramma(), s.getNumero());

        if (exist != null) {
            s.setKey(exist.getKey());
            s.setVersion(exist.getVersion());
            ((GuidaTV_DataLayer) request.getAttribute("datalayer")).getStagioneDAO().update(s);

        } else {
            ((GuidaTV_DataLayer) request.getAttribute("datalayer")).getStagioneDAO().create(s);
        }

        return true;

    }

    private boolean action_crudEpisodio(HttpServletRequest request, HttpServletResponse response) throws DataException {

        String titolo = request.getParameter("titolo");
        int num = SecurityLayer.checkNumeric(request.getParameter("numero"));
        int programmaID = SecurityLayer.checkNumeric(request.getParameter("programma"));
        int numStagione = SecurityLayer.checkNumeric(request.getParameter("numeroStagione"));

        if (num == 0 || programmaID == 0 || numStagione == 0) {
            return false;
        }

        Episodio e = new Episodio_Imp();
        e.setTitolo(titolo);
        e.setNumero(num);
        e.setSerie(((GuidaTV_DataLayer) request.getAttribute("datalayer")).getProgrammaDAO().read(programmaID));
        e.setStagione(((GuidaTV_DataLayer) request.getAttribute("datalayer")).getStagioneDAO().getStagioneByProgramma(e.getSerie(), numStagione));

        Episodio exist = ((GuidaTV_DataLayer) request.getAttribute("datalayer")).getEpisodioDAO().checkExistence(e);

        if (exist != null) {
            e.setKey(exist.getKey());
            e.setVersion(exist.getVersion());
            ((GuidaTV_DataLayer) request.getAttribute("datalayer")).getEpisodioDAO().update(e);

        } else {
            ((GuidaTV_DataLayer) request.getAttribute("datalayer")).getEpisodioDAO().create(e);
        }

        return true;
    }

    private boolean action_crudGenere(HttpServletRequest request, HttpServletResponse response) throws DataException {
        if (request.getParameter("nome") != null && request.getParameter("nome") != "") {
            Genere g = ((GuidaTV_DataLayer) request.getAttribute("datalayer")).getGenereDAO().getGenereByName(request.getParameter("nome"));
            if (g == null) {
                g = new Genere_Imp();
                g.setNome(request.getParameter("nome"));
                ((GuidaTV_DataLayer) request.getAttribute("datalayer")).getGenereDAO().create(g);
                return true;
            }
        }

        return false;
    }

    public String create_Img(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Part file_to_upload = request.getPart("img");
        File uploaded_file = File.createTempFile(((Immagine) request.getAttribute("immagine")).getKey() + "_" + ((Immagine) request.getAttribute("immagine")).getNome() + "_", "." + ((Immagine) request.getAttribute("immagine")).getTipo(), new File("/home/leonardo/NetBeansProjects/web_eng_2020/src/main/webapp/images"));

        try ( InputStream is = file_to_upload.getInputStream();  OutputStream os = new FileOutputStream(uploaded_file)) {
            byte[] buffer = new byte[1024];
            int read;
            while ((read = is.read(buffer)) > 0) {
                os.write(buffer, 0, read);
            }
        }

        return uploaded_file.getName();
    }

    public Immagine img_fromRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, DataException {

        Immagine i = new Immagine_Imp();
        i.setNome(request.getParameter("imgName"));
        i.setTipo(request.getParameter("imgType"));
        i.setTaglia(request.getPart("img").getSize());

        Immagine existImg = ((GuidaTV_DataLayer) request.getAttribute("datalayer")).getImmagineDAO().checkExistence(i.getNome());
        if (existImg != null) {
            i = existImg;
        } else {

            ((GuidaTV_DataLayer) request.getAttribute("datalayer")).getImmagineDAO().create(i);
            i = ((GuidaTV_DataLayer) request.getAttribute("datalayer")).getImmagineDAO().checkExistence(i.getNome());
        }

        if (existImg == null) {
            request.setAttribute("immagine", i);
            i.setNome(create_Img(request, response));
            System.out.println(i.getNome());
            ((GuidaTV_DataLayer) request.getAttribute("datalayer")).getImmagineDAO().update(i);

        }

        return i;
    }
}
