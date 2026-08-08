# Mantém os stack traces legíveis no Play Console.
# Sem isso, os crashes chegam com nomes ofuscados e sem número de linha.
# Lembre-se de enviar o mapping.txt junto com cada release.
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# Compose, Room, Hilt, Firebase e AdMob já publicam suas próprias regras
# de consumo (consumer-rules.pro) dentro dos .aar — não replique aqui.
# Regras `-keep` amplas impedem o R8 de encolher e otimizar o código.
