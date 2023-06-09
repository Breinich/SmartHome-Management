#include <QByteArray>
#include <QNetworkReply>
#include <QRegularExpression>
#include <QDirIterator>
#include <QJsonObject>
#include <QJsonDocument>
#include "networkcommunication.h"
#include "room.h"
#include "ipaddress.h"

NetworkCommunication::NetworkCommunication(QObject* pParent) : QObject(pParent), m_userId(-1)
{
    QString host = "localhost";
    m_baseUrl = "http://" + host + ":8081/api/v1/smarthome";
    m_authPath = "/auth/signup";
    m_loginPath = "/auth/login";
    m_aboutMePath = "/auth/me";
    m_logoutPath = "/auth/logout";
    m_roomsPath = "/rooms";
    m_sensorsPath = "/sensors";
    m_actuatorsPath = "/actuators";
    m_sensorDataPath = "/data";
    m_latestSensorDataPath = "/sensordata/sensor";
    m_commandPath = "/command";

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
                if(pReply->request().url().path().contains(m_latestSensorDataPath))
                {
                    emit communicationFinished();
                    handleGetLatestDataBySensorIdResponse(pReply);
                }
                else if(pReply->request().url().path().endsWith(m_authPath))
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
                    else if (m_listReplys.at(i).first == "PUT" || m_listReplys.at(i).first == "POST")
                    {
                        handleResponseForSensorsPerRoomUpdate(pReply);
                    }
                }
                else if(pReply->request().url().path().endsWith(m_actuatorsPath))
                {
                    emit communicationFinished();
                    if (m_listReplys.at(i).first == "PUT" || m_listReplys.at(i).first == "POST")
                    {
                        handleResponseForActuatorsPerRoomUpdate(pReply);
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
                else if (pReply->request().url().path().contains(m_sensorDataPath))
                {
                    emit communicationFinished();
                    if (m_listReplys.at(i).first == "GET")
                    {
                        handleGetLastHourOfSensorDataResponse(pReply);
                    }
                }
                else if (pReply->request().url().path().contains(m_sensorsPath))
                {
                    emit communicationFinished();
                    if (m_listReplys.at(i).first == "DEL")
                    {
                        handleResponseForSensorsPerRoomUpdate(pReply);
                    }
                }
                else if (pReply->request().url().path().contains(m_actuatorsPath))
                {
                    emit communicationFinished();
                    if (m_listReplys.at(i).first == "DEL")
                    {
                        handleResponseForActuatorsPerRoomUpdate(pReply);
                    }
                }
                else if (pReply->request().url().path().contains(m_aboutMePath))
                {
                    emit communicationFinished();
                    if (m_listReplys.at(i).first == "GET")
                    {
                        handleAboutMeResponse(pReply);
                    }
                }
                else if (pReply->request().url().path().endsWith(m_commandPath))
                {
                    emit communicationFinished();
                    if (m_listReplys.at(i).first == "POST")
                    {
                        handleSendCommandResponse(pReply);
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
            aboutMe(m_authenticator.user());
        }
        else
        {
            reportErrorToUser(pReply);
        }
    }
}

void NetworkCommunication::aboutMe(const QString& email)
{
    sendRequest(m_aboutMePath + "?email=" + m_authenticator.user(), true);
}

void NetworkCommunication::handleAboutMeResponse(QNetworkReply *pReply)
{
    if(pReply)
    {
        QNetworkReply::NetworkError err = pReply->error();
        if(err == QNetworkReply::NoError)
        {
            QByteArray byteArrayResponse(pReply->readAll());
            QString strResponse(QString::fromUtf8(byteArrayResponse));

            QString strId = getJsonValue(strResponse, "userId", 0);
            bool bOk;
            int id = strId.toInt(&bOk);
            if(bOk)
            {
                //TODO: get roles
                m_userId = id;
            }
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
                    address = matchedIPAddress(address);
                    if(!address.isEmpty())
                    {
                        emit addSensorsPerRoomListItem(id, name, type, address);
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


            static QRegularExpression regexpActuatorId("(\"actuatorId\"\\s{0,1}:)");
            int count = strResponse.count(regexpActuatorId);

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
                    address = matchedIPAddress(address);
                    if(!address.isEmpty())
                    {
                        emit addActuatorsPerRoomListItem(id, name, type, address);
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
                        address = matchedIPAddress(address);
                        if(!address.isEmpty())
                        {
                            emit addSensor(id, name, type, address, roomId);
                        }
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

void NetworkCommunication::getLastHourOfSensorData(int sensorId)
{
    qint64 now = QDateTime::currentMSecsSinceEpoch();
    qint64 oneHourAgo = now - 3600000;
    sendRequest(m_sensorsPath + "/" + QString::number(sensorId) + m_sensorDataPath + "/" + QString::number(oneHourAgo) + "/" + QString::number(now), true);
}

void NetworkCommunication::handleGetLastHourOfSensorDataResponse(QNetworkReply *pReply)
{
    if(pReply)
    {
        QNetworkReply::NetworkError err = pReply->error();
        if(err == QNetworkReply::NoError)
        {
            QByteArray byteArrayResponse(pReply->readAll());
            QString strResponse(QString::fromUtf8(byteArrayResponse));

            static QRegularExpression regexpDataId("(\"dataId\":)");
            int count = strResponse.count(regexpDataId);

            long now = QDateTime::currentMSecsSinceEpoch();
            
            for(int i=0; i < count; i++)
            {
            
                QString strValue = getJsonValue(strResponse, "value", i);
                bool bOk;
                int value = strValue.toInt(&bOk);
                if(bOk)
                {
                    QString strTimestamp = getJsonValue(strResponse, "timestamp", i);
                    long timestamp = strTimestamp.toLongLong(&bOk);
                    if(bOk)
                    {
                        emit addSensorStatisticData( qRound((timestamp - now) * 0.001), value);
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

void NetworkCommunication::createSensor(const QString &name, const QString &type, const QString &address, int roomId)
{
    QJsonObject json;
    json.insert("name", name);
    json.insert("type", type);
    json.insert("apiEndpoint", "https://" + address + "/");

    QJsonObject jsonRoom;
    jsonRoom.insert("roomId", roomId);
    json.insert("room", jsonRoom);
    
    QJsonDocument doc = QJsonDocument(json);

    sendPostOrPut(m_sensorsPath, doc.toJson(QJsonDocument::Compact), true, true);
}

void NetworkCommunication::updateSensor(int sensorId, const QString &name, const QString &type, const QString &address, int roomId)
{
    QJsonObject json;
    json.insert("sensorId", sensorId);
    json.insert("name", name);
    json.insert("type", type);
    json.insert("apiEndpoint", "https://" + address + "/");

    QJsonObject jsonRoom;
    jsonRoom.insert("roomId", roomId);
    json.insert("room", jsonRoom);
    
    QJsonDocument doc = QJsonDocument(json);

    sendPostOrPut(m_sensorsPath, doc.toJson(QJsonDocument::Compact), false, true);
}

void NetworkCommunication::updateActuator(int actuatorId, const QString &name, const QString &type, const QString &address, int roomId)
{
    QJsonObject json;
    json.insert("actuatorId", actuatorId);
    json.insert("name", name);
    json.insert("type", type);
    json.insert("apiEndpoint", "https://" + address + "/");

    QJsonObject jsonRoom;
    jsonRoom.insert("roomId", roomId);
    json.insert("room", jsonRoom);
    
    QJsonDocument doc = QJsonDocument(json);

    sendPostOrPut(m_actuatorsPath, doc.toJson(QJsonDocument::Compact), false, true);
}

void NetworkCommunication::deleteActuator(int actuatorId)
{
    sendDelete(m_actuatorsPath + "/" + QString::number(actuatorId), true);
}

void NetworkCommunication::deleteSensor(int sensorId)
{
    sendDelete(m_sensorsPath + "/" + QString::number(sensorId), true);
}


void NetworkCommunication::createActuator(const QString &name, const QString &type, const QString &address, int roomId)
{
    QJsonObject json;
    json.insert("name", name);
    json.insert("type", type);
    json.insert("apiEndpoint", "https://" + address + "/");
    
    QJsonObject jsonRoom;
    jsonRoom.insert("roomId", roomId);
    json.insert("room", jsonRoom);

    QJsonDocument doc = QJsonDocument(json);

    sendPostOrPut(m_actuatorsPath, doc.toJson(QJsonDocument::Compact), true, true);
}


void NetworkCommunication::handleResponseForSensorsPerRoomUpdate(QNetworkReply *pReply){
    if(pReply)
    {
        QNetworkReply::NetworkError err = pReply->error();
        if(err == QNetworkReply::NoError)
        {
            emit sensorsPerRoomUpdateNeeded();
        }
        else
        {
            reportErrorToUser(pReply);
        }
    }
}

void NetworkCommunication::handleResponseForActuatorsPerRoomUpdate(QNetworkReply *pReply){
    if(pReply)
    {
        QNetworkReply::NetworkError err = pReply->error();
        if(err == QNetworkReply::NoError)
        {
            emit actuatorsPerRoomUpdateNeeded();
        }
        else
        {
            reportErrorToUser(pReply);
        }
    }
}

void NetworkCommunication::getLatestDataBySensorId(int sensorId) {
    sendRequest(m_latestSensorDataPath + "/" + QString::number(sensorId), true, false);
}

void NetworkCommunication::handleGetLatestDataBySensorIdResponse(QNetworkReply *pReply)
{
    if(pReply)
    {
        QNetworkReply::NetworkError err = pReply->error();
        if(err == QNetworkReply::NoError)
        {
            QByteArray byteArrayResponse(pReply->readAll());
            QJsonParseError parseError;
            QJsonDocument doc = QJsonDocument::fromJson(byteArrayResponse, &parseError);
            if(parseError.error == QJsonParseError::NoError)
            {
                QJsonObject object = doc.object();
                QString sensorKey("sensor");
                QJsonValue jsonSensor = object.value(sensorKey);
                if(!jsonSensor.isObject())
                {
                    emit reportError("Unexpected value format with JSon key: " + sensorKey);
                    return;
                }
                QJsonObject sensorValue = jsonSensor.toObject();
                QString sensorIdKey("sensorId");
                QJsonValue jsonSensorId = sensorValue.value(sensorIdKey);
                int sensorIdValue = jsonSensorId.toInt();
                QString valueKey("value");
                QJsonValue jsonValue = object.value(valueKey);
                int valueValue = jsonValue.toInt();

                emit updateSensorValue(sensorIdValue, valueValue);
            }
            else
            {
                emit reportError(parseError.errorString());
            }
        }
        else
        {
            reportErrorToUser(pReply);
        }
    }
}

void NetworkCommunication::sendCommand(const QString& type, int roomId, int value) {
    QJsonObject json;
    json.insert("consequenceType", type);
    json.insert("consequenceValue", value);

    QJsonObject jsonRoom;
    jsonRoom.insert("roomId", roomId);
    json.insert("room", jsonRoom);

    QJsonObject jsonUser;
    jsonUser.insert("userId", m_userId);
    json.insert("user", jsonUser);

    QJsonDocument doc = QJsonDocument(json);

    sendPostOrPut(m_commandPath, doc.toJson(QJsonDocument::Compact), true, true);
}

void NetworkCommunication::handleSendCommandResponse(QNetworkReply *pReply)
{
    if(pReply)
    {
        QNetworkReply::NetworkError err = pReply->error();
        if(err == QNetworkReply::NoError)
        {
            emit commandActivated();
        }
        else
        {
            reportErrorToUser(pReply);
        }
    }
}

QString NetworkCommunication::matchedIPAddress(const QString& sampleText)
{
    QString address(sampleText);
    QRegularExpressionMatch match = IpAddress::getRegexpIPv4().match(address);
    if(match.hasMatch())
    {
        address = match.captured();
    }
    else
    {
        match = IpAddress::getRegexpIPv6().match(address);
        if(match.hasMatch())
        {
            address = match.captured();
        }
    }
    if(!match.hasMatch())
    {
        address = "";
    }
    return address;
}
