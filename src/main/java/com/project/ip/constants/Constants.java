package com.project.ip.constants;

public final class Constants
{
    public static final class EndPoints
    {
        public final static String DEFAULT = "/";
        public final static String SIGN_UP = "/signup";
        public final static String CHANGE_PASSWORD = "/changepassword";
        public final static String RESET_PASSWORD = "/resetpassword";
        public final static String LOGIN = "/login";
        public final static String HOME = "/home";
        public final static String GET_RESET_EMAIL = "/getresetemail";
        public final static String CONFIRMATION_CODE = "/confirmationcode";
        public final static String MEDICAL_RECORD = "/medicalrecord";
        public final static String CHAT = "/chat";
        public final static String YOUR_CHATS = "/yourchats";
        public final static String ADD_PACIENT = "/addpacient";
        public final static String LOGOUT = "/logout";
        public final static String SEND_MESSAGE = "/sendmessage";
        public final static String NEW_MESSAGE = "/topic/new_message/";
        public final static String GRAPH = "/graph";
        public final static String ALARMS = "/alarms";
        public final static String STUFF_MENU = "/stuffmenu";
        public final static String SENSOR_SETTINGS = "/sensorsettings";
        public final static String ADD_ALLERGY = "/addallergy";
        public final static String ERROR = "/error";
        public final static String ADMIN = "/admin";
        public final static String ASSIGN_PACIENT_TO_MEDIC = "/addtomedic";
    }

    public static final class Redirects
    {
        public final static String DEFAULT = "redirect:" + EndPoints.DEFAULT;
        public final static String SIGN_UP = "redirect:" + EndPoints.SIGN_UP;
        public final static String CHANGE_PASSWORD = "redirect:" + EndPoints.CHANGE_PASSWORD;
        public final static String RESET_PASSWORD = "redirect:" + EndPoints.RESET_PASSWORD;
        public final static String LOGIN = "redirect:" + EndPoints.LOGIN;
        public final static String HOME = "redirect:" + EndPoints.HOME;
        public final static String GET_RESET_EMAIL = "redirect:" + EndPoints.GET_RESET_EMAIL;
        public final static String CONFIRMATION_CODE = "redirect:" + EndPoints.CONFIRMATION_CODE;
        public final static String MEDICAL_RECORD = "redirect:" + EndPoints.MEDICAL_RECORD;
        public final static String CHAT = "redirect:" + EndPoints.CHAT;
        public final static String YOUR_CHATS = "redirect:" + EndPoints.YOUR_CHATS;
        public final static String GRAPH = "redirect:" + EndPoints.GRAPH;
        public final static String ALARMS = "redirect:" + EndPoints.ALARMS;
        public final static String ADD_ALLERGY = "redirect:" + EndPoints.ADD_ALLERGY;
        public final static String ERROR = "redirect:" + EndPoints.ERROR;
        public final static String ADMIN = "redirect:" + EndPoints.ADMIN;
    }

    public static final class Attributes
    {
        public final static String USER = "user";
        public final static String LOGGED_IN_USER = "loggedInUser";
        public final static String LOGGED_IN_USER_TYPE = "loggedInUserType";
        public final static String POP_UP_MESSAGE = "popupMessage";
        public final static String PASSWORD_RESETER_EMAIL = "passwordReseterEmail";
        public final static String CAN_CHANGE_PASSWORD = "canChangePassword";
        public final static String MEDICAL_RECORD = "medicalRecord";
        public final static String LOGGED_IN_USER_ID = "loggedInUserId";
        public final static String CHAT = "chat";
        public final static String CHAT_HISTORY = "chatHistory";
        public final static String CHAT_ID = "chatId";
        public final static String CONTENT = "content";
        public final static String SENDER_ID = "senderId";
        public final static String SENSORS_DATA = "sensorsData";
        public final static String ALARMS = "alarms";
        public final static String PACIENT_LIST = "pacientList";
        public final static String USER_ID = "userId";
        public final static String SENSORS_SETTINGS = "sensorsSettings";
        public final static String ALLERGY = "allergy";
        public final static String EXAMINATION = "examination";
        public final static String FOUND_USERS = "foundUsers";
        public final static String USER_LIST = "userList";
        public final static String IS_ADMIN = "isAdmin";
        public final static String HAS_DISPLAYED_USERS = "hasDisplayedUsers";
    }

    public static final class ErrorMessages
    {
        public final static String CHANGE_PASSWORD_ERROR = "Eroare la schimbarea parolei!";
        public final static String LOGIN_ERROR = "Eroare la logare! Username sau parola gresite!";
        public final static String CHECK_EMAIL_ERROR = "Eroare! Incercati din nou!";
        public final static String CONFIRMATION_CODE_ERROR = "Eroare! Incercati sa introduceti un email de resetare inainte sa intrati pe aceasta pagina!";
        public final static String RESET_PASSWORD_ERROR = "Eroare! Trebuie confirmata cererea de schimbare a parolei cu codul de pe email!";
        public final static String WRONG_ERROR_CODE = "Codul introdus este gresit!";
        public final static String SIGN_UP_ERROR = "Eroare la crearea contului! CNP-ul deja exista!";
        public final static String NOT_LOGGED_IN = "Eroare! Trebuie sa fiti logat pentru a putea accesa aceasta pagina!";
        public final static String RETRIEVING_CHAT_ERROR = "Eroare la vizualizarea chatului!";
        public final static String SENDING_MESSAGE_ERROR = "Eroare la trimiterea mesajului!";
        public final static String RESOLVING_ALARM_ERROR = "Eroare la rezolvarea alarmei! Incercati din nou!";
        public final static String NOT_STUFF_ERROR = "Eroare! Nu aveti drepturile necesare pentru a vizualiza aceasta pagina!";
        public final static String UPDATE_SENSOR_SETTINGS_ERROR = "Eroare! Valorile senzorilor nu s-au actualizat";
        public final static String ADD_ALLERGY_ERROR = "Eroare la adaugarea alergiei!";
        public final static String CANNOT_CREATE_CHAT_ERROR = "Eroare la crearea chatului!";
    }

    public static final class Messages
    {
        public final static String CHECK_EMAIL = "Cod de resetare al parolei trimis pe email!";
        public final static String RESET_PASSWORD_SUCCESS = "Parola resetata cu succes!";
        public final static String SENSOR_SETTINGS_UPDATE_SUCCESS = "Senzorii au fost actualizati cu succes!";
        public final static String MEDICAL_RECORD_UPDATE_SUCCESS = "Consultatie adaugata cu succes!";
    }

    public static final class Errors
    {
        public final static Integer DUMMY_ID = -1;
    }

    public static final class RequestParameters
    {
        public final static String NEW_PASSWORD = "newPassword";
        public final static String RESET_EMAIL = "resetEmail";
        public final static String CONFIRMATION_CODE = "confirmationCode";
        public final static String ID = "id";
        public final static String MESSAGE_TO_ADD = "messageToAdd";
        public final static String CHAT_ID = "chatId";
        public final static String NUMBER_OF_DAYS = "numberOfDays";
        public final static String SENSOR_TYPES_BIT_MASK = "sensorTypesBitMask";
        public final static String SENSOR_TYPE_STRING = "sensorTypeString";
        public final static String IS_RESOLVED = "isResolved";
        public final static String NEW_SENSOR_SETTINGS = "newSensorSettings";
        public final static String ALLERGY = "allergy";
        public final static String NAME = "name";
        public final static String OTHER_USER_ID = "otherUserId";
        public final static String EXAMINATION = "examination";
        public final static String EXAMINATION_DATE = "examinationDate";
        public final static String DIAGNOSTIC = "diagnostic";
        public final static String CURE = "cure";
        public final static String RECOMANDATION = "recomandation";
        public final static String ACCESS_TYPE = "accessType";
        public final static String PACIENT_ID = "pacientId";
    }

    public static final class ParametersKeys
    {
        public final static String EMAIL_ADDRESS = "emailAddress";
        public final static String NEW_PASSWORD = "newPassword";
        public final static String CONFIRMATION_CODE = "confirmationCode";
        public final static String DATE = "date";
        public final static String USER = "user";
        public final static String EXAMINATIONS = "examinations";
        public final static String ALLERGIES = "allergies";
    }

    public static final class RemoteServerAddresses
    {
        public final static String CLOUD_SERVER = "http://ip_server_remote:5000"; //to change with real ip
    }

    public static final class RemoteServerEndPoints
    {
        public final static String ADD_USER = "/add_user";
        public final static String LOGIN = "/login";
        public final static String CHANGE_PASSWORD = "/change_password";
        public final static String RESET_PASSWORD_EMAIL = "/reset_password_email";
        public final static String CHECK_CONFIRMATION_CODE = "/check_confirmation_code";
        public final static String GET_MEDICAL_RECORD = "/get_medical_record";
        public final static String ADD_MESSAGE = "/add_message";
        public final static String GET_CHAT = "/get_chat";
        public final static String GET_CHAT_HISTORY = "/get_chat_history";
        public final static String GET_SENSORS_DATA = "/get_sensors_data";
        public final static String GET_ALARMS = "/get_alarms";
        public final static String RESOLVE_ALARM = "/resolve_alarm";
        public final static String GET_PACIENTS = "/get_pacients";
        public final static String GET_SENSORS_SETTINGS = "/get_sensors_settings";
        public final static String UPDATE_SENSORS_SETTINGS = "/update_sensors_settings";
        public final static String ADD_ALLERGY = "/add_allergy";
        public final static String SEARCH_USER = "/search_user";
        public final static String ADD_CHAT = "/add_chat";
        public final static String GET_ALL_USERS = "/get_all_users";
        public final static String ADD_EXAMINATION = "/add_examination";
        public final static String DELETE_USER = "/delete_user";
        public final static String CHANGE_USER_ACCESS_TYPE = "/change_user_access_type";
        public final static String ADD_PACIENT_TO_MEDIC = "/add_pacient_to_medic";
    }

    public static final class Logs
    {
        public final static String WEBSOCKET_CONNECTING_FAILED = "WebSocket session connecting failed (couldn't get user id)";
        public final static String WEBSOCKET_CONNECTING_SUCCESS = "WebSocket session connecting success for user with id = ";
        public final static String WEBSOCKET_DISCONNECTING_FAILED = "WebSocket session disconnecting failed (couldn't get user id)";
        public final static String WEBSOCKET_DISCONNECTING_SUCCESS = "WebSocket session disconnecting success for user with id = ";
        public final static String ADD_MESSAGE_ERROR = "Error at adding message to DB!";
        public final static String EMPTY_SENSOR_VALUE = "Value of the sensor is null!";
        public final static String ADD_EXAMINATION_ERROR = "Error at adding examination to DB!";
    }

    public static final class Prefixes
    {
        public final static String TOPIC = "/topic";
        public final static String CHAT = "/chat";
    }
}