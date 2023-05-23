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


signals:
    void communicationStarted();
    void communicationFinished();
    void reportError(QString strError);
    void accountCreated();
    void successfulSignIn();
    void successfulLogOut();
    void addRoomsListItem(int id1, QString name1, QString image1, bool bVisible1, int id2, QString name2, QString image2, bool bVisible2);

private slots:
    void slotReplyFinished(QNetworkReply *pReply);

private:
    void reportErrorToUser(QNetworkReply *pReply);
    void sendRequest(const QString& strPath, bool bAuthenticate = false);
    void sendPost(const QString& strPath, const QByteArray& body);
    void handleCreateAccountResponse(QNetworkReply *pReply);
    void handleLoginResponse(QNetworkReply *pReply);
    void handleLogoutResponse(QNetworkReply *pReply);
    void handleGetRoomsResponse(QNetworkReply *pReply);

    QNetworkAccessManager* m_pNetManager;
    QAuthenticator m_authenticator;
    QString m_baseUrl;
    QString m_authPath;
    QString m_loginPath;
    QString m_logoutPath;
    QString m_roomsPath;

};

#endif // NETWORKCOMMUNICATION_H
