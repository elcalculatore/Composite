import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import java.io.*;

public class TestTree {
    private static int branche;

    public static String pathToXml(String path) {

        File directory = new File(path);
        String xml = "\n" + "<directory name = " + "\"" + directory.getName() + "\"" + ">";
        File[] liste = directory.listFiles();
        for (File objet : liste) {
            if (objet.isFile()) {
                xml = xml + "\n" + "\t" + "<file name = " + "\"" + objet.getName() + "\"" + "/>";
            } else if (objet.isDirectory()) {
                xml = xml + pathToXml(objet.getAbsolutePath());
            }
        }

        xml = xml + "\n" + "</directory>" + "\n";
        return xml;

    }

    public static Composant insertion(Element elmnt) {
        Dossier racine = new Dossier(elmnt.getAttribute("name"), branche);
        NodeList nodes = elmnt.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            if (nodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
                Element el = (Element) nodes.item(i);
                if (el.getNodeName().equals("file")) {
                    Composant monfichier = new Fichier(el.getAttribute("name"), racine.getBranche() + 1);
                    racine.ajouter(monfichier);
                } else if (el.getNodeName().equals("directory")) {
                    branche = racine.getBranche() + 1;
                    racine.ajouter(insertion(el));
                    branche--;
                }
            }
        }
        return racine;
    }

    public static Composant xmlToDoc(String xml) throws ParserConfigurationException, SAXException, IOException {
        String xmlStr = "<?xml version=\"1.0\"?>" + xml;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        StringBuilder xmlBuilder = new StringBuilder();
        xmlBuilder.append(xmlStr);
        ByteArrayInputStream input = new ByteArrayInputStream(xmlBuilder.toString().getBytes("UTF-8"));
        Document doc = builder.parse(input);
        Element element = doc.getDocumentElement();
        return insertion(element);
    }

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {


        String xml1 = pathToXml("c:\\DUT");
        System.out.println(xml1);

        String xml2 = pathToXml("c:\\DUT");
        Composant objet = xmlToDoc(xml2);
        objet.afficher();
    }
}
