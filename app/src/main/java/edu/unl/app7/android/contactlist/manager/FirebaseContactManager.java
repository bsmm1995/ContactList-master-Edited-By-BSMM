package edu.unl.app7.android.contactlist.manager;

import com.firebase.client.Firebase;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.unl.app7.android.contactlist.model.Contact;


public class FirebaseContactManager {

    private static FirebaseContactManager instance = null;
    private Firebase firebaseContactRef;
    private HashMap<String, Contact> contactHashMap = new HashMap<>();

    public static FirebaseContactManager getInstance() {
        if (instance == null) {
            instance = new FirebaseContactManager();
        }
        return instance;
    }

    private FirebaseContactManager() {
        firebaseContactRef = new Firebase("https://contactlist-bd9c0.firebaseio.com/");
    }


    public void getContactFromServer(ValueEventListener listener) {
        firebaseContactRef.keepSynced(true);
        Query queryRef = firebaseContactRef.orderByKey();
        queryRef.addListenerForSingleValueEvent(listener);
    }


    public List<Contact> getAllContacts() {
        return new ArrayList<>(contactHashMap.values());
    }

    public Contact getContactByObjectId(String objectId) {
        return contactHashMap.get(objectId);
    }


    public void addContactHashMap(Contact contact) {
        this.contactHashMap.put(contact.getObjectId(), contact);
    }
}
