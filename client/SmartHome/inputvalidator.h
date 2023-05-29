#ifndef INPUTVALIDATOR_H
#define INPUTVALIDATOR_H
#include <QRegularExpression>

class InputValidator : public QObject
{
    Q_OBJECT
public:
    InputValidator(QObject* pParent = nullptr);
    ~InputValidator();
    Q_INVOKABLE bool validateName(const QString& strName);
    Q_INVOKABLE bool validateEmail(const QString& strMail);
    Q_INVOKABLE bool validatePassword(const QString& strPwd);
    Q_INVOKABLE bool validateEntityName(const QString& strName);
private:
    const QRegularExpression m_nameRegexp;
    const QRegularExpression m_emailRegexp;
    const QRegularExpression m_passwordRegexp;
    const QRegularExpression m_entityNameRegexp;
};

#endif // INPUTVALIDATOR_H
