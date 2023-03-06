
// Afterwards, write an XML parser (either in DOM or StAX) that will count the number of movies released by decade.
import javax.xml.parsers.*;
import org.xml.sax.*;
import org.w3c.dom.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class parser {

   
    public void analyze(String filepath) {
        DocumentBuilderFactory dbf  = DocumentBuilderFactory.newInstance();
                            

        //Following the same structure of the commentary
        DocumentBuilder dbuild = null;
        try {
            dbuild =  dbf.newDocumentBuilder();
            dbuild.setErrorHandler(new  ErrorHandler() {
            public void error(SAXParseException ex) {
                                System.err.println(ex);
                                }
            public void fatalError(SAXParseException ex){
                                System.err.println(ex);
                                }
            public void warning(SAXParseException ex) {
                                System.out.println(ex);
                                } 
            });
            }
        catch (ParserConfigurationException  ex) {
                    System.err.println(ex);
                    System.exit(1);
            }

        Document document = null;

        try {
            document = dbuild.parse(new File(filepath));
        }
        catch (SAXException  ex) {
            System.err.println(ex);
        }  
        catch (IOException  ex) {
            System.err.println(ex);
        } 


        NodeList nodeList =  document.getDocumentElement().getChildNodes();
        ParseDecades(nodeList);
    }


    //Unlike the recursive function in the commentary, this function will only check the top level nodes
    //Using this class will require some knowledge of the structure of the data. 
    public void  ParseDecades(NodeList nodeList) {
        HashMap<Integer, ArrayList<String>> myHash = new HashMap<Integer,ArrayList<String>>();
        if (nodeList == null)  return;

        for (int i = 0; i <  nodeList.getLength(); i++) {

            
            //This is at the DVD element level
            Node child_node = nodeList.item(i);
            if  (child_node.getNodeType() == Node.ELEMENT_NODE) {
                NodeList inner_nodes = child_node.getChildNodes();
                if (inner_nodes == null) {
                    return;
                }
                String title = "";
                    

                //This is at the level immediately below DVD, including title, discs, price etc. 
                for (int j = 0; j < inner_nodes.getLength(); j++){
                    Node inner_node = inner_nodes.item(j);
                    String tagname = inner_node.getNodeName();
                    if (tagname.equals("title")){
                        title = inner_node.getTextContent().trim();
                    }
                    if (tagname.equals("release_year")){
                        String strYear = inner_node.getTextContent().trim();
                        Integer intYear = Integer.parseInt(strYear.substring(0, 3));
                        ArrayList<String> df = myHash.get(intYear);

                        if (df == null){
                            df = new ArrayList<String>();
                            df.add(title);
                            myHash.put(intYear, df);
                        }else{
                            df.add(title);
                        }
                    }

                }

            }
        }

        //Here I will format and output the movies by decade
        for(Integer i: myHash.keySet()){
            System.out.println("################");
            System.out.println("Movies From the " + i +"0's");
            System.out.println("################");
            for(String n: myHash.get(i)){
                System.out.println(n);
            }
        }
        
 } 

    public static void  main(String[] args) {

    parser domDVDReader  = new parser();
    domDVDReader.analyze("dvd.xml");


    }
}