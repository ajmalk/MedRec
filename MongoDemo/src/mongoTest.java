import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoException;
import com.mongodb.WriteConcern;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBCursor;
import com.mongodb.ServerAddress;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Set;



public class mongoTest {

	/**
	 * @param args
	 * @throws UnknownHostException 
	 */
	public static void main(String[] args) throws UnknownHostException {
		// TODO Auto-generated method stub
		MongoClientURI theURI = new MongoClientURI("mongodb://MedRec:CherryPie@ds037987.mongolab.com:37987/medrec_development");  //Creates the URI object
        MongoClient aMongo = new MongoClient(theURI); // Uses the URI to establish a connection
        DB db = aMongo.getDB( "medrec_development" ); // Creates the database object
        Set<String> colls = db.getCollectionNames(); // Test code: Gets collection names

        for (String s : colls) {
            System.out.println(s); // Test code: Prints collection names
        }
        
        DBCollection userCollection = db.getCollection("Users");  // Test code 2: Gets the Users collection
        BasicDBObject userQuery = new BasicDBObject("name", "testUser");  // Test code 2: Creates a query, looking for the "name" field to be "testUser"
        
        DBCursor cursor = userCollection.find(userQuery); // Test code 2: Creates the cursor, which tracks the active object
        DBObject theUser;
        
        try {
           while(cursor.hasNext()) {  // Test code 2: While there's a next object...
               theUser = cursor.next();  
               System.out.println(theUser); // Test code 2: ... it'll print it to console.
               System.out.println(theUser.get("name")); // Test code 2: And then print just the name.
           }
        } finally {
           cursor.close();  // Closes the cursor.
        }
	}

 // image grab code
	String newFileName = patient.get("pic").toString();
	GridFS gfsPhoto = new GridFS(db, "photo");
	GridFSDBFile imageForOutput = gfsPhoto.findOne(newFileName);
	imageForOutput.writeTo("c:\\JavaWebHostingNew.png");  // line to change

}
