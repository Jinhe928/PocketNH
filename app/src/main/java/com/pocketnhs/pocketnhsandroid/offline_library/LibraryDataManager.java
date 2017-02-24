package com.pocketnhs.pocketnhsandroid.offline_library;

import android.util.Log;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.couchbase.lite.Emitter;
import com.couchbase.lite.Mapper;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryEnumerator;
import com.couchbase.lite.QueryRow;
import com.couchbase.lite.View;
import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.Manager;
import com.couchbase.lite.android.AndroidContext;
import com.pocketnhs.pocketnhsandroid.globals.ApplicationConsts;
import com.pocketnhs.pocketnhsandroid.globals.ApplicationState;
import com.pocketnhs.pocketnhsandroid.globals.ApplicationUtils;
import com.pocketnhs.pocketnhsandroid.server.ServerDataManager;
import com.pocketnhs.pocketnhsandroid.server.transfer_objects.NHSCHQArticle;
import com.pocketnhs.pocketnhsandroid.server.transfer_objects.NHSCondition;
import com.pocketnhs.pocketnhsandroid.server.transfer_objects.NHSOrganisation;
import com.pocketnhs.pocketnhsandroid.server.transfer_objects.NHSService;
import com.pocketnhs.pocketnhsandroid.ui.SaveFinishedListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by MacBook Pro on 9/1/2016.
 */

public class LibraryDataManager implements ImageLoader.ImageListener {

    private static LibraryDataManager mInstance;
    private SaveFinishedListener mListener;


    private Database mCouchDatabaseOrganisations;
    private Database mCouchDatabaseServices;
    private Database mCouchDatabaseConditions;
    private Manager mCouchDBManager;

    private String VIEW_ORGANISATIONS = "organisations_view";
    private String VIEW_SERVICES = "services_view";
    private String VIEW_CONDITIONS = "conditions_view";
    private String VIEW_ARTICLES = "articles_view";


    public static synchronized LibraryDataManager getInstance() {
        if (mInstance == null) {
            mInstance = new LibraryDataManager();
            mInstance.initInstance();
        }
        return mInstance;
    }

    private void initInstance() {

        setupDatabaseOrganisation();
        setupDatabaseConditions();
        setupOrganisationsView();
        setupServicesView();
        setupConditionsView();
        setupArticlesView();

    }

    private void setupOrganisationsView() {
        View productView = mCouchDatabaseOrganisations.getView(VIEW_ORGANISATIONS);
        productView.setMap(new Mapper() {
            @Override
            public void map(Map<String, Object> document, Emitter emitter) {
                emitter.emit(document.get("mID"), document);
            }
        }, "1");
    }

    private void setupServicesView() {
        View productView = mCouchDatabaseOrganisations.getView(VIEW_SERVICES);
        productView.setMap(new Mapper() {
            @Override
            public void map(Map<String, Object> document, Emitter emitter) {
                emitter.emit(document.get("mID"), document);
            }
        }, "1");
    }

    private void setupConditionsView() {
        View productView = mCouchDatabaseConditions.getView(VIEW_CONDITIONS);
        productView.setMap(new Mapper() {
            @Override
            public void map(Map<String, Object> document, Emitter emitter) {
                emitter.emit(document.get("mTitle"), document);
            }
        }, "1");
    }

    private void setupArticlesView() {
        View productView = mCouchDatabaseConditions.getView(VIEW_ARTICLES);
        productView.setMap(new Mapper() {
            @Override
            public void map(Map<String, Object> document, Emitter emitter) {
                emitter.emit(document.get("mTitle"), document);
            }
        }, "1");
    }


    public void setupDatabaseOrganisation() {
        // Create a manager
        Boolean error = false;
        if (mCouchDBManager == null) {
            try {
                mCouchDBManager = new Manager(new AndroidContext(ApplicationState.getAppContext()), Manager.DEFAULT_OPTIONS);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        // Create or open the database named app
        try {
            mCouchDatabaseOrganisations = mCouchDBManager.getDatabase(ApplicationConsts.LIBRARY_DB_ORGANISATIONS);
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
            error = true;
        }
    }

    public void setupDatabaseServices() {
        // Create a manager
        Boolean error = false;
        if (mCouchDBManager == null) {
            try {
                mCouchDBManager = new Manager(new AndroidContext(ApplicationState.getAppContext()), Manager.DEFAULT_OPTIONS);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        // Create or open the database named app
        try {
            mCouchDatabaseServices = mCouchDBManager.getDatabase(ApplicationConsts.LIBRARY_DB_SERVICES);
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
            error = true;
        }
    }

    public void setupDatabaseConditions() {
        // Create a manager
        Boolean error = false;
        if (mCouchDBManager == null) {
            try {
                mCouchDBManager = new Manager(new AndroidContext(ApplicationState.getAppContext()), Manager.DEFAULT_OPTIONS);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        // Create or open the database named app
        try {
            mCouchDatabaseConditions = mCouchDBManager.getDatabase(ApplicationConsts.LIBRARY_DB_CONDITIONS);
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
            error = true;
        }
    }

    public void toggleSavedToFavorites(NHSOrganisation organisation, SaveFinishedListener listener) {
        mListener = listener;
        if (organisation!=null) {
            Log.w("toggle", "name:" + organisation.mName + " - " + organisation.mID);
        }
        if (!isSavedToFavorites(organisation)) {
            ServerDataManager.getInstance().getStaticMap(organisation,this);
        }
    }

    /*public void toggleSavedToFavorites(NHSService service) {
        if (isSavedToFavorites(service)) {
            deleteFromFavorites(service);
        } else {
            saveToFavorites(service);
        }
    }*/

    public boolean isSavedToFavorites(NHSOrganisation organisation) {
        boolean isSaved = false;
        Query query = mCouchDatabaseOrganisations.getView(VIEW_ORGANISATIONS).createQuery();
        query.setMapOnly(true);
        QueryEnumerator result = null;
        try {
            result = query.run();
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
            return false;
        }
        for (Iterator<QueryRow> it = result; it.hasNext();){
            QueryRow row = it.next();
            Map organisationProperties = (Map) row.getValue();
            if (organisationProperties.get("mID").equals(organisation.mID)){
                isSaved = true;
            }
        }
        return isSaved;
    }

    public boolean isSavedToFavorites(NHSService service) {
        boolean isSaved = false;
        Query query = mCouchDatabaseOrganisations.getView(VIEW_ORGANISATIONS).createQuery();
        query.setMapOnly(true);
        QueryEnumerator result = null;
        try {
            result = query.run();
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
            return false;
        }
        for (Iterator<QueryRow> it = result; it.hasNext();){
            QueryRow row = it.next();
            Map organisationProperties = (Map) row.getValue();
            if (organisationProperties.get("mID").equals(service.mID)){
                isSaved = true;
            }
        }
        return isSaved;
    }

    private boolean saveToFavorites(NHSOrganisation organisation) {
        clearFavoriteGPs();
        boolean error = false;
        Document document = mCouchDatabaseOrganisations.createDocument();
        Map properties = organisation.getPropertiesHashMap();
        try {
            document.putProperties(properties);
        } catch (CouchbaseLiteException e) {
            Log.e(" saving document error", e.toString());
            error = true;
        }
        return !error;
    }


    private void clearFavoriteGPs(){
        {
            List<NHSOrganisation> organisations = new ArrayList<>();
            Query query = mCouchDatabaseOrganisations.getView(VIEW_ORGANISATIONS).createQuery();
            query.setMapOnly(true);
            QueryEnumerator result = null;
            try {
                result = query.run();
            } catch (CouchbaseLiteException e) {
                e.printStackTrace();
            }
            for (Iterator<QueryRow> it = result; it.hasNext();){
                QueryRow row = it.next();
                try {
                    row.getDocument().delete();
                } catch (CouchbaseLiteException e) {
                    e.printStackTrace();
                }
            }

        }
    }
    /*private boolean saveToFavorites(NHSService service) {
        boolean error = false;
        Document document = mCouchDatabaseOrganisations.createDocument();
        Map properties = service.getPropertiesHashMap();
        try {
            document.putProperties(properties);
        } catch (CouchbaseLiteException e) {
            Log.e(" saving document error", e.toString());
            error = true;
        }
        return !error;
    }
    */

    public List<NHSOrganisation> getSavedOrganisations() {
        List<NHSOrganisation> organisations = new ArrayList<>();
        Query query = mCouchDatabaseOrganisations.getView(VIEW_ORGANISATIONS).createQuery();
        query.setMapOnly(true);
        QueryEnumerator result = null;
        try {
            result = query.run();
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
        for (Iterator<QueryRow> it = result; it.hasNext();){
            QueryRow row = it.next();
            Map organisationProperties = (Map) row.getValue();
            NHSOrganisation organisation = NHSOrganisation.getOrganization(organisationProperties);
            organisations.add(organisation);
        }
        return organisations;
    }


    public List<NHSService> getSavedServices() {
        List<NHSService> services = new ArrayList<>();
        Query query = mCouchDatabaseServices.getView(VIEW_SERVICES).createQuery();
        query.setMapOnly(true);
        QueryEnumerator result = null;
        try {
            result = query.run();
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
        for (Iterator<QueryRow> it = result; it.hasNext();){
            QueryRow row = it.next();
            Map serviceProperties = (Map) row.getValue();
            NHSService service = NHSService.getService(serviceProperties);
            services.add(service);
        }
        return services;
    }

    public List<NHSCondition> getSavedConditions() {
        List<NHSCondition> conditions = new ArrayList<>();
        Query query = mCouchDatabaseConditions.getView(VIEW_CONDITIONS).createQuery();
        query.setMapOnly(true);
        QueryEnumerator result = null;
        try {
            result = query.run();
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
            return conditions;
        }
        for (Iterator<QueryRow> it = result; it.hasNext();){
            QueryRow row = it.next();
            Map conditionProperties = (Map) row.getValue();
            NHSCondition condition = NHSCondition.getCondition(conditionProperties);
            conditions.add(condition);
        }
        return conditions;
    }

    public boolean isSavedToFavorites(NHSCondition condition) {
        boolean isSaved = false;
        Query query = mCouchDatabaseConditions.getView(VIEW_CONDITIONS).createQuery();
        query.setMapOnly(true);
        QueryEnumerator result = null;
        try {
            result = query.run();
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
            return false;
        }
        for (Iterator<QueryRow> it = result; it.hasNext();){
            QueryRow row = it.next();
            Map conditionProperties = (Map) row.getValue();
            if (conditionProperties.get("mTitle").equals(condition.mTitle)){
                isSaved = true;
            }
        }
        return isSaved;
    }

    public boolean isSavedToFavorites(NHSCHQArticle article) {
        boolean isSaved = false;
        Query query = mCouchDatabaseConditions.getView(VIEW_ARTICLES).createQuery();
        query.setMapOnly(true);
        QueryEnumerator result = null;
        try {
            result = query.run();
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
            return false;
        }
        for (Iterator<QueryRow> it = result; it.hasNext();){
            QueryRow row = it.next();
            Map conditionProperties = (Map) row.getValue();
            if (conditionProperties.get("mTitle").equals(article.mTitle)){
                isSaved = true;
            }
        }
        return isSaved;
    }

    public NHSOrganisation getMyGP(){
        NHSOrganisation organisation = null;
        Query query = mCouchDatabaseOrganisations.getView(VIEW_ORGANISATIONS).createQuery();
        query.setMapOnly(true);
        QueryEnumerator result = null;
        try {
            result = query.run();
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
        for (Iterator<QueryRow> it = result; it.hasNext();){
            QueryRow row = it.next();
            Map organisationProperties = (Map) row.getValue();
             organisation = NHSOrganisation.getOrganization(organisationProperties);
        }
        return organisation;
    }

    private boolean saveToFavorites(NHSCondition condition, boolean isAZ) {
        boolean error = false;
        Document document = mCouchDatabaseConditions.createDocument();
        Map properties = condition.getPropertiesHashMap();
        if (isAZ){
            properties.put("type","AZ");
        }else{
            properties.put("type", "SC");
        }
        try {
            document.putProperties(properties);
        } catch (CouchbaseLiteException e) {
            Log.e(" saving document error", e.toString());
            error = true;
        }
        return !error;
    }

    private boolean saveToFavorites(NHSCHQArticle article) {
        boolean error = false;
        Document document = mCouchDatabaseConditions.createDocument();
        Map properties = article.getPropertiesHashMap();
        properties.put("type","CHQ");
        try {
            document.putProperties(properties);
        } catch (CouchbaseLiteException e) {
            Log.e(" saving document error", e.toString());
            error = true;
        }
        return !error;
    }


    public boolean toggleConditionSavedToFavorites(NHSCondition condition, boolean isAZ) {
        if (!isSavedToFavorites(condition)){
            return saveToFavorites(condition, isAZ);
        }else{
            return deleteFromFavorites(condition);
        }
    }

    public boolean toggleArticleSavedToFavorites(NHSCHQArticle article) {
        if (!isSavedToFavorites(article)){
            return saveToFavorites(article);
        }else{
            return deleteFromFavorites(article);
        }
    }

    private boolean deleteFromFavorites(NHSCondition condition) {
        return false;
    }

    private boolean deleteFromFavorites(NHSCHQArticle article) {
        return false;
    }

    @Override
    public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {

        if (response.getBitmap()!=null) {
            Log.w("LDM", "saving bitmap");
            NHSOrganisation organisation = ServerDataManager.getInstance().mSaveOrganisationObject;
            ApplicationUtils.saveBitmapToFile(response.getBitmap(), organisation);
            if (saveToFavorites(organisation)){
                if (mListener !=null){
                    mListener.saveFinished(SaveFinishedListener.MESSAGE_OK, organisation);
                }
            }else{
                if (mListener !=null){
                    mListener.saveFinished("Couldn't save to My GP", organisation);
                }
            }
        }else{
            Log.w("LDM", "bitmap null");
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(ApplicationState.getAppContext(),"Couldn't save organisation", Toast.LENGTH_LONG).show();
    }

    public void removeFromLibrary(NHSCondition conditionToDelete) {
        List<NHSCondition> conditions = new ArrayList<>();
        Query query = mCouchDatabaseConditions.getView(VIEW_CONDITIONS).createQuery();
        query.setMapOnly(true);
        QueryEnumerator result = null;
        try {
            result = query.run();
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
        Document delete = null;
        for (Iterator<QueryRow> it = result; it.hasNext();){
            QueryRow row = it.next();
            Map conditionProperties = (Map) row.getValue();
            NHSCondition condition = NHSCondition.getCondition(conditionProperties);
            if(condition.mTitle.equals(conditionToDelete.mTitle)) {
              delete = row.getDocument();
            }
        }
        if (delete!=null){
            try {
                delete.delete();
            } catch (CouchbaseLiteException e) {

            }
        }



    }
}
