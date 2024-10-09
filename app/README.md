Não é necessario nenhuma configuração especifica para subir o projeto local
 - Para gerar arquivos localmente na pasta ./app, use o parametro 'export.saveToFile' com valor true (apenas para fins de teste local)


Exemplo de chamada HTTP via Insomnia/Postman
 - http://localhost:8080/api/export/download?type=EXCEL&flow=b
 - http://localhost:8080/api/export/download?type=CSV&flow=a

type = excel/csv
flow = a/b
