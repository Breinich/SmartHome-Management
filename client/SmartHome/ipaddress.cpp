#include "ipaddress.h"

IpAddress::IpAddress():
    m_regexpIPv4("(\\b25[0-5]|\\b2[0-4][0-9]|\\b[01]?[0-9][0-9]?)(\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)){3}") // https://ihateregex.io/expr/ip/
    ,m_regexpIPv6("(([0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|::(ffff(:0{1,4}){0,1}:){0,1}((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])|([0-9a-fA-F]{1,4}:){1,4}:((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]))") // https://ihateregex.io/expr/ipv6/
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
