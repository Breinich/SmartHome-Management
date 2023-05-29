#ifndef IPADDRESS_H
#define IPADDRESS_H
#include <QRegularExpression>

class IpAddress: public QObject
{
    Q_OBJECT
public:
    IpAddress();
    Q_INVOKABLE bool isIPv4(const QString& address);
    Q_INVOKABLE bool isIPv6(const QString& address);
    Q_INVOKABLE QString getPartAt(int partIdx, const QString& address);
private:
    const QRegularExpression m_regexpIPv4;
    const QRegularExpression m_regexpIPv6;
};

#endif // IPADDRESS_H
