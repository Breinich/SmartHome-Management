#include "inputvalidator.h"

InputValidator::InputValidator(QObject* pParent) : QObject(pParent)
    ,m_nameRegexp("^[a-zA-Z]+$")
    ,m_emailRegexp("^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$") // source https://www.abstractapi.com/tools/email-regex-guide
    ,m_passwordRegexp("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=._-])(?=\\S+$).{8,}$")
    ,m_entityNameRegexp("^[a-zA-Z0-9 ]+$")
{

}

InputValidator::~InputValidator()
{

}

bool InputValidator::validateName(const QString& strName)
{
    QRegularExpressionMatch match = m_nameRegexp.match(strName);
    return match.hasMatch();
}

bool InputValidator::validateEmail(const QString& strMail)
{
    QRegularExpressionMatch match = m_emailRegexp.match(strMail);
    return match.hasMatch();
}

bool InputValidator::validatePassword(const QString& strPwd)
{
    QRegularExpressionMatch match = m_passwordRegexp.match(strPwd);
    return match.hasMatch();
}

bool InputValidator::validateEntityName(const QString& strName)
{
    QRegularExpressionMatch match = m_entityNameRegexp.match(strName);
    return match.hasMatch();
}
