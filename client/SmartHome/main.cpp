
#include <QtWidgets/QApplication>
#include <QQmlApplicationEngine>
#include <QIcon>
#include "networkcommunication.h"
#include "inputvalidator.h"
#include "ipaddress.h"


int main(int argc, char *argv[])
{
    QApplication app(argc, argv);
    app.setWindowIcon(QIcon(":/SmartHome/logo.png"));

    QQmlApplicationEngine engine;
    qmlRegisterType<NetworkCommunication>("itcatcetc.networkcommunication", 1, 0, "NetworkCommunication");
    qmlRegisterType<InputValidator>("itcatcetc.inputvalidator", 1, 0, "InputValidator");
    qmlRegisterType<IpAddress>("itcatcetc.ipaddress", 1, 0, "IpAddress");

    const QUrl url(u"qrc:/SmartHome/Main.qml"_qs);
    QObject::connect(&engine, &QQmlApplicationEngine::objectCreated,
        &app, [url](QObject *obj, const QUrl &objUrl) {
            if (!obj && url == objUrl)
                QCoreApplication::exit(-1);
        }, Qt::QueuedConnection);
    engine.load(url);

    return app.exec();
}
