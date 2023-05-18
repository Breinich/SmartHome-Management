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

private slots:
    void slotReplyFinished(QNetworkReply *pReply);

private:
    void handleCreateAccountResponse(QNetworkReply *pReply);

    QNetworkAccessManager* m_pNetManager;
    QString m_baseUrl;
    QString m_authPath;
};

#endif // NETWORKCOMMUNICATION_H
