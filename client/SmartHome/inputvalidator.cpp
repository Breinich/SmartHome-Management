#include "inputvalidator.h"

InputValidator::InputValidator(QObject* pParent) : QObject(pParent)
    ,m_nameRegexp("^[a-zA-Z]+$")
    ,m_emailRegexp("^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$") // source https://www.abstractapi.com/tools/email-regex-guide
    ,m_passwordRegexp("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=._-])(?=\\S+$).{8,}$")
{

}

InputValidator::~InputValidator()
{

}

bool InputValidator::validateName(const QString& strName)
{
    QRegularExpressionMatch match = m_nameRegexp.match(strName);
    bool bValid = match.hasMatch();
    return bValid;
}

bool InputValidator::validateEmail(const QString& strMail)
{
    QRegularExpressionMatch match = m_emailRegexp.match(strMail);
    bool bValid = match.hasMatch();
    return bValid;
}

bool InputValidator::validatePassword(const QString& strPwd)
{
    QRegularExpressionMatch match = m_passwordRegexp.match(strPwd);
    bool bValid = match.hasMatch();
    return bValid;
}
