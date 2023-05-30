#ifndef NETWORKCOMMUNICATION_H
#define NETWORKCOMMUNICATION_H
#include <QNetworkAccessManager>
#include <QAuthenticator>

class NetworkCommunication : public QObject
{
    Q_OBJECT
public:
    NetworkCommunication(QObject* pParent = nullptr);
    ~NetworkCommunication();
    Q_INVOKABLE void createAccount(const QString& firstName, const QString& lastName, const QString& email, const QString& password);
    Q_INVOKABLE void login(const QString& email, const QString& password);
    Q_INVOKABLE void logout();
    Q_INVOKABLE void getRooms();
    Q_INVOKABLE void createRoom(const QString& name, const QString& picture);
    Q_INVOKABLE void updateRoom(int roomId, const QString& name, const QString& picture);
    Q_INVOKABLE void deleteRoom(int roomId);
    Q_INVOKABLE void getSensorsPerRoom(int roomId);
    Q_INVOKABLE void getActuatorsPerRoom(int roomId);
    Q_INVOKABLE void getAllSensors();
    Q_INVOKABLE void getLastHourOfSensorData(int sensorId);
    Q_INVOKABLE void createSensor(const QString& name, const QString& type, const QString& address, int roomId);
    Q_INVOKABLE void updateSensor(int sensorId, const QString& name, const QString& type, const QString& address, int roomId);
    Q_INVOKABLE void deleteSensor(int sensorId);
    Q_INVOKABLE void createActuator(const QString& name, const QString& type, const QString& address, int roomId);
    Q_INVOKABLE void updateActuator(int actuatorId, const QString& name, const QString& type, const QString& address, int roomId);
    Q_INVOKABLE void deleteActuator(int sensorId);



signals:
    void communicationStarted();
    void communicationFinished();
    void reportError(QString strError);
    void accountCreated();
    void successfulSignIn();
    void successfulLogOut();
    void addRoomsListItem(int id1, QString name1, QString image1, bool bVisible1, int id2, QString name2, QString image2, bool bVisible2);
    void roomsUpdateNeeded();
    void addSensorsPerRoomListItem(int id, QString name, QString type, QString address);
    void addActuatorsPerRoomListItem(int id, QString name, QString type, QString address);
    void addSensor(int id, QString name, QString type, QString address, int roomId);
    void addSensorStatisticData(int xValue, int yValue); //x value is time, data with first timestamp is 0, data with second timestamp is secondTimeStamp - firstTimeStamp (elapsed time), y value is value of sensor
    void sensorsPerRoomUpdateNeeded();
    void actuatorsPerRoomUpdateNeeded();



private slots:
    void slotReplyFinished(QNetworkReply *pReply);

private:
    void reportErrorToUser(QNetworkReply *pReply);
    void addAuthHeader(QNetworkRequest& request);
    void sendRequest(const QString& strPath, bool bAuth = false, bool bNotifyStart = true);
    void sendDelete(const QString& strPath, bool bAuth = false, bool bNotifyStart = true);
    void sendPostOrPut(const QString& strPath, const QByteArray& body, bool bPost, bool bAuth = false, bool bNotifyStart = true);
    void handleCreateAccountResponse(QNetworkReply *pReply);
    void handleLoginResponse(QNetworkReply *pReply);
    void handleLogoutResponse(QNetworkReply *pReply);
    void handleGetRoomsResponse(QNetworkReply *pReply);
    void handleResponseForRoomsUpdate(QNetworkReply *pReply);
    void handleGetSensorsPerRoomResponse(QNetworkReply *pReply);
    void handleGetActuatorsPerRoomResponse(QNetworkReply *pReply);
    void handleGetAllSensorsResponse(QNetworkReply *pReply);
    void handleGetLastHourOfSensorDataResponse(QNetworkReply *pReply); //emit addSensorStatisticData
    void handleResponseForSensorsPerRoomUpdate(QNetworkReply *pReply); //emit sensorsPerRoomUpdateNeeded
    void handleResponseForActuatorsPerRoomUpdate(QNetworkReply *pReply); //emit actuatorsPerRoomUpdateNeeded

    //Note: can not handle array as value
    QString getJsonValue(const QString& sampleText, const QString& key, int idx, const QString& afterKey = "");
    QString cutBeginAndEndQuotes(const QString& sampleText);

    QNetworkAccessManager* m_pNetManager;
    QAuthenticator m_authenticator;
    QString m_baseUrl;
    QString m_authPath;
    QString m_loginPath;
    QString m_logoutPath;
    QString m_roomsPath;
    QString m_sensorsPath;
    QString m_actuatorsPath;
    QString m_sensorDataPath;
    QList<QPair<QString, QNetworkReply*>> m_listReplys;
};

#endif // NETWORKCOMMUNICATION_H
