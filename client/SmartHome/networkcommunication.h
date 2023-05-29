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
    Q_INVOKABLE void getSensors(int roomId);
    Q_INVOKABLE void getActuators(int roomId);


signals:
    void communicationStarted();
    void communicationFinished();
    void reportError(QString strError);
    void accountCreated();
    void successfulSignIn();
    void successfulLogOut();
    void addRoomsListItem(int id1, QString name1, QString image1, bool bVisible1, int id2, QString name2, QString image2, bool bVisible2);
    void roomsUpdateNeeded();
    void addSensorsListItem();

private slots:
    void slotReplyFinished(QNetworkReply *pReply);

private:
    void reportErrorToUser(QNetworkReply *pReply);
    void addAuthHeader(QNetworkRequest& request);
    void sendRequest(const QString& strPath, bool bAuth = false);
    void sendDelete(const QString& strPath, bool bAuth = false);
    void sendPostOrPut(const QString& strPath, const QByteArray& body, bool bPost, bool bAuth = false);
    void handleCreateAccountResponse(QNetworkReply *pReply);
    void handleLoginResponse(QNetworkReply *pReply);
    void handleLogoutResponse(QNetworkReply *pReply);
    void handleGetRoomsResponse(QNetworkReply *pReply);
    void handleResponseForRoomsUpdate(QNetworkReply *pReply);

    //Note: can not handle array as value
    QString getJsonValue(const QString& sampleText, const QString& key, int idx);
    QString cutBeginAndEndQuotes(const QString& sampleText);

    QNetworkAccessManager* m_pNetManager;
    QAuthenticator m_authenticator;
    QString m_baseUrl;
    QString m_authPath;
    QString m_loginPath;
    QString m_logoutPath;
    QString m_roomsPath;
    QList<QPair<QString, QNetworkReply*>> m_listReplys;
};

#endif // NETWORKCOMMUNICATION_H
