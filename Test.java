import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import java.io.*;

public class Test {
    private static int niveau;

    public static String pathToXml(String path) {

        File directory = new File(path);
        String xmlstring = "\n" + "<directory name = " + "\"" + directory.getName() + "\"" + ">";
        File[] liste = directory.listFiles();
        for (File objet : liste) {
            if (objet.isFile()) {
                xmlstring = xmlstring + "\n" + "\t" + "<file name = " + "\"" + objet.getName() + "\"" + "/>";
            } else if (objet.isDirectory()) {
                xmlstring = xmlstring + pathToXml(objet.getAbsolutePath());
            }
        }

        xmlstring = xmlstring + "\n" + "</directory>" + "\n";
        return xmlstring;

    }

    public static Composant insertion(Element elmnt) {
        Dossier home = new Dossier(elmnt.getAttribute("name"), niveau);
        NodeList nodes = elmnt.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            if (nodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
                Element el = (Element) nodes.item(i);
                if (el.getNodeName().equals("file")) {
                    Composant monfichier = new Fichier(el.getAttribute("name"), home.getNiveau() + 1);
                    home.ajouter(monfichier);
                } else if (el.getNodeName().equals("directory")) {
                    niveau = home.getNiveau() + 1;
                    home.ajouter(insertion(el));
                    niveau--;
                }
            }
        }
        return home;
    }

    public static Composant xmlToDoc(String xmlstring) throws ParserConfigurationException, SAXException, IOException {
        String xmlStr = "<?xml version=\"1.0\"?>" + xmlstring;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        StringBuilder xmlStringBuilder = new StringBuilder();
        xmlStringBuilder.append(xmlStr);
        ByteArrayInputStream input = new ByteArrayInputStream(xmlStringBuilder.toString().getBytes("UTF-8"));
        Document doc = builder.parse(input);
        Element element = doc.getDocumentElement();
        return insertion(element);
    }

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {


        String xmlString1 = pathToXml("c:\\DUT");
        System.out.println(xmlString1);

        String xmlString2 = pathToXml("c:\\DUT");
        Composant root = xmlToDoc(xmlString2);
        root.afficher();
    }
}
