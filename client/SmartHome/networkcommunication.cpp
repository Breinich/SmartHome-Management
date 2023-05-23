#include "networkcommunication.h"
#include <QByteArray>
#include <QNetworkReply>

NetworkCommunication::NetworkCommunication(QObject* pParent) : QObject(pParent)
{
    QString host = "localhost";
    m_baseUrl = "http://" + host + ":8081/api/v1/smarthome";
    m_authPath = "/auth/signup";
    m_loginPath = "/auth/login";
    m_logoutPath = "/auth/logout";
    m_roomsPath = "/rooms";

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
    if(pReply)
    {
        if(pReply->request().url().path().endsWith(m_authPath))
        {
            emit communicationFinished();
            handleCreateAccountResponse(pReply);
        }
        else if(pReply->request().url().path().endsWith(m_loginPath))
        {
            emit communicationFinished();
            handleLoginResponse(pReply);
        }
        else if(pReply->request().url().path().endsWith(m_logoutPath))
        {
            emit communicationFinished();
            handleLogoutResponse(pReply);
        }
        else if(pReply->request().url().path().endsWith(m_roomsPath))
        {
            emit communicationFinished();
            handleGetRoomsResponse(pReply);
        }
    }
}

void NetworkCommunication::reportErrorToUser(QNetworkReply *pReply)
{
    if(pReply)
    {
        QByteArray byteArrayResponse(pReply->readAll());
        QString strResponse(QString::fromUtf8(byteArrayResponse));
        emit reportError("Operation unsuccessful" + (strResponse.isEmpty() ? "" : (": " + strResponse)));
    }
}

void NetworkCommunication::sendRequest(const QString& strPath, bool bAuthenticate)
{
    emit communicationStarted();
    QNetworkRequest request;
    request.setUrl(QUrl(m_baseUrl+strPath));
    request.setTransferTimeout();
    if(bAuthenticate)
    {
        QString credentialsString = QString("%1:%2").arg(m_authenticator.user(), m_authenticator.password());
        QByteArray base64Encoded(credentialsString.toUtf8().toBase64());
        QByteArray headerData = "Basic " + base64Encoded;
        request.setRawHeader("Authorization", headerData);
    }
    m_pNetManager->get(request);
}

void NetworkCommunication::sendPost(const QString& strPath, const QByteArray& body)
{
    emit communicationStarted();
    QNetworkRequest request;
    request.setUrl(QUrl(m_baseUrl+strPath));
    request.setHeader(QNetworkRequest::ContentTypeHeader, "application/json");
    request.setTransferTimeout();
    m_pNetManager->post(request, body);
}

void NetworkCommunication::createAccount(const QString& firstName, const QString& lastName, const QString& email, const QString& password)
{
    QByteArray body("{\"firstName\":\"" + firstName.toUtf8() + "\",\"lastName\":\"" + lastName.toUtf8() + "\",\"email\":\"" +
                             email.toUtf8() + "\",\"password\":\"" + password.toUtf8() + "\"}");
    sendPost(m_authPath, body);
}

void NetworkCommunication::handleCreateAccountResponse(QNetworkReply *pReply)
{
    if(pReply)
    {
        QNetworkReply::NetworkError err = pReply->error();
        if(err == QNetworkReply::NoError)
        {
            emit accountCreated();
        }
        else
        {
            reportErrorToUser(pReply);
        }
    }
}

void NetworkCommunication::login(const QString& email, const QString& password)
{
    QByteArray body("{\"email\":\"" + email.toUtf8() + "\",\"password\":\"" + password.toUtf8() + "\"}");
    m_authenticator.setUser(email);
    m_authenticator.setPassword(password);
    sendPost(m_loginPath, body);
}

void NetworkCommunication::handleLoginResponse(QNetworkReply *pReply)
{
    if(pReply)
    {
        QNetworkReply::NetworkError err = pReply->error();
        if(err == QNetworkReply::NoError)
        {
            emit successfulSignIn();
        }
        else
        {
            reportErrorToUser(pReply);
        }
    }
}

void NetworkCommunication::logout()
{
    sendRequest(m_logoutPath);
}

void NetworkCommunication::handleLogoutResponse(QNetworkReply *pReply)
{
    if(pReply)
    {
        QNetworkReply::NetworkError err = pReply->error();
        if(err == QNetworkReply::NoError)
        {
            emit successfulLogOut();
        }
        else
        {
            reportErrorToUser(pReply);
        }
    }
}

void NetworkCommunication::getRooms()
{
    sendRequest(m_roomsPath, true);
}

void NetworkCommunication::handleGetRoomsResponse(QNetworkReply *pReply)
{
    if(pReply)
    {
        QNetworkReply::NetworkError err = pReply->error();
        if(err == QNetworkReply::NoError)
        {
            QByteArray byteArrayResponse(pReply->readAll());
            QString strResponse(QString::fromUtf8(byteArrayResponse));
            QMap<int, QString> mapRooms;

            while(strResponse.contains("roomId"))
            {
                int startPos = strResponse.indexOf(":") + 1;
                int endPos = strResponse.indexOf(",", startPos);
                QString strId = strResponse.sliced(startPos, endPos-startPos);
                int id = -2;
                bool bOk;
                int tmpId = strId.toInt(&bOk);
                if(bOk)
                {
                    id = tmpId;
                }
                strResponse = strResponse.last(strResponse.size()-endPos);

                startPos = strResponse.indexOf(":") + 2;
                endPos = strResponse.indexOf("}", startPos) - 1;
                QString name = strResponse.sliced(startPos, endPos-startPos);
                strResponse = strResponse.last(strResponse.size()-endPos);
                mapRooms.insert(id, name);
            }

            //TODO: read photo from file

            bool bPlusAdded = false;
            QList<int> listId = mapRooms.keys();
            for(int i=0; i<listId.size(); i+=2)
            {
                int key1 = listId.at(i);
                if(i+1 < listId.size())
                {
                    int key2 = listId.at(i+1);
                    emit addRoomsListItem(key1, mapRooms.value(key1), "unknown.png", true, key2, mapRooms.value(key2), "unknown.png", true);
                }
                else
                {
                    emit addRoomsListItem(key1, mapRooms.value(key1), "unknown.png", true, -1, "Add room", "add.png", true);
                    bPlusAdded = true;
                }
            }

            if(!bPlusAdded)
            {
                emit addRoomsListItem(-1, "Add room", "add.png", true, -2, "", "unknown.png", false);
            }

        }
        else
        {
            reportErrorToUser(pReply);
        }
    }
}
