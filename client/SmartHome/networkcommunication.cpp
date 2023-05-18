#include "networkcommunication.h"
#include <QByteArray>
#include <QNetworkReply>

NetworkCommunication::NetworkCommunication(QObject* pParent)
{
    QString host = "localhost";
    m_baseUrl = "http://" + host + ":8081/api/v1/smarthome";
    m_authPath = "/auth/signup";

    m_pNetManager = new QNetworkAccessManager(pParent);
    m_pNetManager->connectToHost(host, 8081);
    bool bConnected = QObject::connect(m_pNetManager, &QNetworkAccessManager::finished,
            this, &NetworkCommunication::slotReplyFinished);

    Q_ASSERT_X(bConnected, "NetworkCommunication", "can not connect signal QNetworkAccessManager::finished - slot NetworkCommunication::slotReplyFinished");
}

NetworkCommunication::~NetworkCommunication()
{
    if(m_pNetManager)
    {
        delete m_pNetManager;
    }
}


void NetworkCommunication::slotReplyFinished(QNetworkReply *pReply)
{
     //Big Big Todo
    if(pReply && pReply->request().url().path().endsWith(m_authPath))
    {
        handleCreateAccountResponse(pReply);
    }
}

void NetworkCommunication::createAccount(const QString& firstName, const QString& lastName, const QString& email, const QString& password)
{
    QNetworkRequest request;
    request.setUrl(QUrl(m_baseUrl+m_authPath));
    request.setHeader(QNetworkRequest::ContentTypeHeader, "application/json");


    QByteArray body("{\"firstName\":\"" + firstName.toUtf8() + "\",\"lastName\":\"" + lastName.toUtf8() + "\",\"email\":\"" +
                             email.toUtf8() + "\",\"password\":\"" + password.toUtf8() + "\"}");

    m_pNetManager->post(request, body);
}

void NetworkCommunication::handleCreateAccountResponse(QNetworkReply *pReply)
{
    //Big Big Todo
    if(pReply)
    {
        QNetworkReply::NetworkError err = pReply->error();
        if(err == QNetworkReply::NoError)
        {
            QByteArray byteArrayResponse(pReply->readAll());
            QString strResponse(QString::fromUtf8(byteArrayResponse));
        }
        else
        {
            QByteArray byteArrayResponse(pReply->readAll());
            QString strResponse(QString::fromUtf8(byteArrayResponse));
        }
    }
}
