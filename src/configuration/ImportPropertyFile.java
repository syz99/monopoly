package configuration;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ImportPropertyFile {

    private Properties prop;
    private InputStream input;
    String propFileName;

    /***
     * Opens property file and loads it as a variable in the Screen classes
     * @param filepath name of properties file in resources
     */
    public ImportPropertyFile(String filepath) {
        prop = new Properties();
        input = ImportPropertyFile.class.getClassLoader().getResourceAsStream(filepath);
        try {
            prop.load(input);
        } catch (IOException e) {
            e.printStackTrace();     //apparently not supposed to just print stack trace but return an exceptions or something
        }
    }

    public void getPropValues(String inp) throws IOException {
        String result = "";
        Properties prop = new Properties();
        String propFileName = inp;

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

        try {
            prop.load(inputStream);

        } catch (IOException e) {
            throw new FileNotFoundException("Property File " + propFileName + " not found in the classpath!");
        }
    }



        public String getProp(String aprop) {
            return prop.getProperty(aprop);
        }

    }


