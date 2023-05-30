#include <QByteArray>
#include <QNetworkReply>
#include <QRegularExpression>
#include <QDirIterator>
#include "networkcommunication.h"
#include "room.h"

NetworkCommunication::NetworkCommunication(QObject* pParent) : QObject(pParent)
{
    QString host = "localhost";
    m_baseUrl = "http://" + host + ":8081/api/v1/smarthome";
    m_authPath = "/auth/signup";
    m_loginPath = "/auth/login";
    m_logoutPath = "/auth/logout";
    m_roomsPath = "/rooms";
    m_sensorsPath = "/sensors";
    m_actuatorsPath = "/actuators";

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
        for(qsizetype  i=0; i<m_listReplys.size(); i++)
        {
            if(m_listReplys.at(i).second == pReply)
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
                    if (m_listReplys.at(i).first == "GET")
                    {
                        handleGetRoomsResponse(pReply);
                    }
                    else if (m_listReplys.at(i).first == "POST" || m_listReplys.at(i).first == "PUT")
                    {
                        handleResponseForRoomsUpdate(pReply);
                    }
                }
                else if(pReply->request().url().path().endsWith(m_sensorsPath))
                {
                    emit communicationFinished();
                    if (m_listReplys.at(i).first == "GET")
                    {
                        handleGetAllSensorsResponse(pReply);
                    }
                }
                else if(pReply->request().url().path().contains(m_roomsPath + m_sensorsPath))
                {
                    emit communicationFinished();
                    if (m_listReplys.at(i).first == "GET")
                    {
                        handleGetSensorsPerRoomResponse(pReply);
                    }
                }
                else if(pReply->request().url().path().contains(m_roomsPath + m_actuatorsPath))
                {
                    emit communicationFinished();
                    if (m_listReplys.at(i).first == "GET")
                    {
                        handleGetActuatorsPerRoomResponse(pReply);
                    }
                }
                else if(pReply->request().url().path().contains(m_roomsPath))
                {
                    emit communicationFinished();
                    if (m_listReplys.at(i).first == "DEL")
                    {
                        handleResponseForRoomsUpdate(pReply);
                    }
                }
                m_listReplys.removeAt(i);
            }
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

void NetworkCommunication::addAuthHeader(QNetworkRequest& request)
{
    QString credentialsString = QString("%1:%2").arg(m_authenticator.user(), m_authenticator.password());
    QByteArray base64Encoded(credentialsString.toUtf8().toBase64());
    QByteArray headerData = "Basic " + base64Encoded;
    request.setRawHeader("Authorization", headerData);
}

void NetworkCommunication::sendRequest(const QString& strPath, bool bAuth, bool bNotifyStart)
{
    if(bNotifyStart)
    {
        emit communicationStarted();
    }
    QNetworkRequest request;
    request.setUrl(QUrl(m_baseUrl+strPath));
    request.setTransferTimeout();
    if(bAuth)
    {
        addAuthHeader(request);
    }
    m_listReplys.append(qMakePair("GET", m_pNetManager->get(request)));
}

void NetworkCommunication::sendDelete(const QString& strPath, bool bAuth, bool bNotifyStart)
{
    if(bNotifyStart)
    {
        emit communicationStarted();
    }
    QNetworkRequest request;
    request.setUrl(QUrl(m_baseUrl+strPath));
    request.setTransferTimeout();
    if(bAuth)
    {
        addAuthHeader(request);
    }
    m_listReplys.append(qMakePair("DEL", m_pNetManager->deleteResource(request)));
}

void NetworkCommunication::sendPostOrPut(const QString& strPath, const QByteArray& body, bool bPost, bool bAuth, bool bNotifyStart)
{
    if(bNotifyStart)
    {
        emit communicationStarted();
    }
    QNetworkRequest request;
    request.setUrl(QUrl(m_baseUrl+strPath));
    request.setHeader(QNetworkRequest::ContentTypeHeader, "application/json");
    request.setTransferTimeout();
    if(bAuth)
    {
        addAuthHeader(request);
    }

    if(bPost)
    {
        m_listReplys.append(qMakePair("POST", m_pNetManager->post(request, body)));
    }
    else
    {
        m_listReplys.append(qMakePair("PUT", m_pNetManager->put(request, body)));
    }
}

QString NetworkCommunication::getJsonValue(const QString& sampleText, const QString& key, int idx, const QString& afterKey)
{
    QString retVal;

    const QRegularExpression regexpKey("(\"" + key +"\"\\s{0,1}:\\s{0,1})");
    const QRegularExpression regexpAfterKey("(\"" + afterKey +"\"\\s{0,1}:\\s{0,1})");
    QRegularExpressionMatch match = afterKey.isEmpty() ? regexpKey.match(sampleText) : regexpAfterKey.match(sampleText);
    qsizetype startPos = match.capturedEnd();
    if(idx>0)
    {
        for(int i=1; i<=idx; i++)
        {
            match = afterKey.isEmpty() ? regexpKey.match(sampleText, startPos) : regexpAfterKey.match(sampleText, startPos);
            startPos = match.capturedEnd();
        }
    }

    if(!afterKey.isEmpty())
    {
        match = regexpKey.match(sampleText, startPos);
        startPos = match.capturedEnd();
    }

    static QRegularExpression regexpEnd("[,}]");
    qsizetype endPos = sampleText.indexOf(regexpEnd, startPos);
    retVal = sampleText.sliced(startPos, endPos-startPos);
    return retVal;
}

QString NetworkCommunication::cutBeginAndEndQuotes(const QString& sampleText)
{
    QString retVal(sampleText);
    if(retVal.at(0) == '\"' && retVal.at(retVal.size()-1) == '\"')
    {
        retVal = retVal.sliced(1, retVal.size()-2);
    }
    return retVal;
}

void NetworkCommunication::createAccount(const QString& firstName, const QString& lastName, const QString& email, const QString& password)
{
    QByteArray body("{\"firstName\":\"" + firstName.toUtf8() + "\",\"lastName\":\"" + lastName.toUtf8() + "\",\"email\":\"" +
                             email.toUtf8() + "\",\"password\":\"" + password.toUtf8() + "\"}");
    sendPostOrPut(m_authPath, body, true);
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
    sendPostOrPut(m_loginPath, body, true);
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
            QList<Room> listRooms;

            static QRegularExpression regexpRoomId("(\"roomId\"\\s{0,1}:)");
            int count = strResponse.count(regexpRoomId);

            for(int i=0; i<count; i++)
            {
                QString strId = getJsonValue(strResponse, "roomId", i);
                bool bOk;
                int id = strId.toInt(&bOk);
                if(bOk)
                {
                    QString name = getJsonValue(strResponse, "name", i);
                    name = cutBeginAndEndQuotes(name);
                    QString photo = getJsonValue(strResponse, "coverPhoto", i);
                    photo = cutBeginAndEndQuotes(photo);
                    listRooms.append(Room(id, name, photo));
                }
            }

            bool bPlusAdded = false;
            for(int i=0; i<listRooms.size(); i+=2)
            {
                Room room1 = listRooms.at(i);
                if(i+1 < listRooms.size())
                {
                    Room room2 = listRooms.at(i+1);
                    emit addRoomsListItem(room1.id(), room1.name(), room1.photo(), true, room2.id(), room2.name(), room2.photo(), true);
                }
                else
                {
                    emit addRoomsListItem(room1.id(), room1.name(), room1.photo(), true, Room::getAddRoomId(), Room::getAddRoomName(), Room::getAddRoomPhoto(), true);
                    bPlusAdded = true;
                }
            }

            if(!bPlusAdded)
            {
                emit addRoomsListItem(Room::getAddRoomId(), Room::getAddRoomName(), Room::getAddRoomPhoto(), true, -2, "", Room::getPhotoInvalid(), false);
            }
        }
        else
        {
            reportErrorToUser(pReply);
        }
    }
}

void NetworkCommunication::createRoom(const QString& name, const QString& picture)
{
    QByteArray body("{\"name\":\"" + name.toUtf8() + "\",\"coverPhoto\":\"" + picture.toUtf8() + "\"}");
    sendPostOrPut(m_roomsPath, body, true, true);
}

void NetworkCommunication::updateRoom(int roomId, const QString& name, const QString& picture)
{
    QByteArray body("{\"roomId\":\"" + QString::number(roomId).toUtf8() + "\",\"name\":\"" + name.toUtf8() +
                    "\",\"coverPhoto\":\"" + picture.toUtf8() + "\"}");
    sendPostOrPut(m_roomsPath, body, false, true);
}

void NetworkCommunication::deleteRoom(int roomId)
{
    sendDelete(m_roomsPath + "/" + QString::number(roomId), true);
}

void NetworkCommunication::handleResponseForRoomsUpdate(QNetworkReply *pReply)
{
    if(pReply)
    {
        QNetworkReply::NetworkError err = pReply->error();
        if(err == QNetworkReply::NoError)
        {
            emit roomsUpdateNeeded();
        }
        else
        {
            reportErrorToUser(pReply);
        }
    }
}

void NetworkCommunication::getSensorsPerRoom(int roomId)
{
    sendRequest(m_roomsPath + m_sensorsPath + "/" + QString::number(roomId), true);
}

void NetworkCommunication::handleGetSensorsPerRoomResponse(QNetworkReply *pReply)
{
    if(pReply)
    {
        QNetworkReply::NetworkError err = pReply->error();
        if(err == QNetworkReply::NoError)
        {
            QByteArray byteArrayResponse(pReply->readAll());
            QString strResponse(QString::fromUtf8(byteArrayResponse));


            static QRegularExpression regexpSensorId("(\"sensorId\"\\s{0,1}:)");
            int count = strResponse.count(regexpSensorId);

            for(int i=0; i<count; i++)
            {
                QString strId = getJsonValue(strResponse, "sensorId", i);
                bool bOk;
                int id = strId.toInt(&bOk);
                if(bOk)
                {
                    QString name = getJsonValue(strResponse, "name", i, "sensorId");
                    name = cutBeginAndEndQuotes(name);
                    QString type = getJsonValue(strResponse, "type", i);
                    type = cutBeginAndEndQuotes(type);
                    QString address = getJsonValue(strResponse, "apiEndpoint", i);
                    address = cutBeginAndEndQuotes(address);
                    QStringList listSplitted = address.split("/");
                    emit addSensorsPerRoomListItem(id, name, type, listSplitted[listSplitted.length()-2]);
                }
            }
        }
        else
        {
            reportErrorToUser(pReply);
        }
    }
}

void NetworkCommunication::getActuatorsPerRoom(int roomId)
{
    sendRequest(m_roomsPath + m_actuatorsPath + "/" + QString::number(roomId), true);
}

void NetworkCommunication::handleGetActuatorsPerRoomResponse(QNetworkReply *pReply)
{
    if(pReply)
    {
        QNetworkReply::NetworkError err = pReply->error();
        if(err == QNetworkReply::NoError)
        {
            QByteArray byteArrayResponse(pReply->readAll());
            QString strResponse(QString::fromUtf8(byteArrayResponse));


            static QRegularExpression regexpSensorId("(\"actuatorId\"\\s{0,1}:)");
            int count = strResponse.count(regexpSensorId);

            for(int i=0; i<count; i++)
            {
                QString strId = getJsonValue(strResponse, "actuatorId", i);
                bool bOk;
                int id = strId.toInt(&bOk);
                if(bOk)
                {
                    QString name = getJsonValue(strResponse, "name", i, "actuatorId");
                    name = cutBeginAndEndQuotes(name);
                    QString type = getJsonValue(strResponse, "type", i);
                    type = cutBeginAndEndQuotes(type);
                    QString address = getJsonValue(strResponse, "apiEndpoint", i);
                    address = cutBeginAndEndQuotes(address);
                    QStringList listSplitted = address.split("/");
                    emit addActuatorsPerRoomListItem(id, name, type, listSplitted[listSplitted.length()-2]);
                }
            }
        }
        else
        {
            reportErrorToUser(pReply);
        }
    }
}

void NetworkCommunication::getAllSensors(){
    sendRequest(m_sensorsPath, true);
}

void NetworkCommunication::handleGetAllSensorsResponse(QNetworkReply *pReply)
{
    if(pReply)
    {
        QNetworkReply::NetworkError err = pReply->error();
        if(err == QNetworkReply::NoError)
        {
            QByteArray byteArrayResponse(pReply->readAll());
            QString strResponse(QString::fromUtf8(byteArrayResponse));


            static QRegularExpression regexpSensorId("(\"sensorId\"\\s{0,1}:)");
            int count = strResponse.count(regexpSensorId);

            for(int i=0; i<count; i++)
            {
                QString strId = getJsonValue(strResponse, "sensorId", i);
                bool bOk;
                int id = strId.toInt(&bOk);
                if(bOk)
                {
                    QString strRoomId = getJsonValue(strResponse, "roomId", i);
                    int roomId = strRoomId.toInt(&bOk);
                    if(bOk)
                    {
                        QString name = getJsonValue(strResponse, "name", i, "sensorId");
                        name = cutBeginAndEndQuotes(name);
                        QString type = getJsonValue(strResponse, "type", i);
                        type = cutBeginAndEndQuotes(type);
                        QString address = getJsonValue(strResponse, "apiEndpoint", i);
                        address = cutBeginAndEndQuotes(address);
                        QStringList listSplitted = address.split("/");
                        emit addSensor(id, name, type, listSplitted[listSplitted.length()-2], roomId);
                    }
                }
            }
        }
        else
        {
            reportErrorToUser(pReply);
        }
    }
}
