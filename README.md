# mail-serverless-app-v1

This project contains following components.

`Lambda Function`
- MailAppFunction - Function to push data to SQS (FIFO/STD).
- ProcessMailFunction - Function to read/pull data to SQS (FIFO/STD) and process and send email.

`API Gateway`
- EmailApiGateway - To expose MailAppFunction as RestFull service.

`SQS Queue (FIFO/STANDARD)`
- EmailMessageQueue - This receives messages from MailAppFunction.
- EmailMessageDeadLetterQueue - This is to handled failed messages which can not reach EmailMessageQueue.

`SAM Templete`
- template.yaml - This contains defination, configuration and integration of AWS services.
- template_std.yaml - This is same as template.yaml but the only difference is that it uses Standard SQS instead of FIFO


## Commands

To build and deploy  application:

```bash
sam build
sam deploy --guided
```

To delete AWSCloudFormation Stack:

```bash
aws cloudformation delete-stack --stack-name mail-app-stack-east
```

