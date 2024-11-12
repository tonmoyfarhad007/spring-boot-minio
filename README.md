Have Docker installed on your pc and run the below command

```Linux:```
mkdir -p ${HOME}/minio/data

docker run \
   -p 9000:9000 \
   -p 9001:9001 \
   --user $(id -u):$(id -g) \
   --name minio1 \
   -e "MINIO_ROOT_USER=ROOTUSER" \
   -e "MINIO_ROOT_PASSWORD=CHANGEME123" \
   -v ${HOME}/minio/data:/data \
   quay.io/minio/minio server /data --console-address ":9001"

   ```Windows:```
   docker run \
   -p 9000:9000 \
   -p 9001:9001 \
   --name minio1 \
   --security-opt "credentialspec=file://path/to/file.json"
   -e "MINIO_ROOT_USER=ROOTUSER" \
   -e "MINIO_ROOT_PASSWORD=CHANGEME123" \
   -v D:\data:/data \
   quay.io/minio/minio server /data --console-address ":9001"
