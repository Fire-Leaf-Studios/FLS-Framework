echo Sending file
HOST='fireleafstudios.uk'
USER='h2n0@fireleafstudios.uk'
PASSWD='Pok954954'

ftp -n -v $HOST << EOT
ascii
user $USER $PASSWD
prompt
cd dl/engine
put ./LatestEngine.jar LatestEngine.jar
bye
echo File sent
EOT
