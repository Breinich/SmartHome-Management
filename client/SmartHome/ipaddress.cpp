#include "ipaddress.h"

IpAddress::IpAddress()
{

}

bool IpAddress::isIPv4(const QString& address)
{
    return m_regexpIPv4.match(address).hasMatch();
}

bool IpAddress::isIPv6(const QString& address)
{
    return m_regexpIPv6.match(address).hasMatch();
}

QString IpAddress::getPartAt(int partIdx, const QString& address)
{
    if(isIPv4(address) && partIdx >= 0 && partIdx < 4)
    {
        QStringList partList = address.split('.');
        return partList.at(partIdx);
    }
    if(isIPv6(address) && partIdx >= 0 && partIdx < 8)
    {
        QStringList partList = address.split(':');
        return partList.at(partIdx);
    }
    return "";
}

const QRegularExpression IpAddress::getRegexpIPv4()
{
    return m_regexpIPv4;
}

const QRegularExpression IpAddress::getRegexpIPv6()
{
    return m_regexpIPv6;
}
