
#include <QGuiApplication>
#include <QQmlApplicationEngine>
#include <QIcon>
#include "networkcommunication.h"


int main(int argc, char *argv[])
{
    QGuiApplication app(argc, argv);
    app.setWindowIcon(QIcon(":/SmartHome/logo.png"));

    QQmlApplicationEngine engine;
    qmlRegisterType<NetworkCommunication>("itcatcetc.networkcommunication", 1, 0, "NetworkCommunication");

    const QUrl url(u"qrc:/SmartHome/Main.qml"_qs);
    QObject::connect(&engine, &QQmlApplicationEngine::objectCreated,
        &app, [url](QObject *obj, const QUrl &objUrl) {
            if (!obj && url == objUrl)
                QCoreApplication::exit(-1);
        }, Qt::QueuedConnection);
    engine.load(url);

    return app.exec();
}
