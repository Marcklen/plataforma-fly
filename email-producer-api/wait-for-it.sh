#!/bin/sh

HOST=$1
PORT=$2
shift 2

until nc -z "$HOST" "$PORT"; do
  echo "Aguardando $HOST:$PORT..."
  sleep 2
done

echo "$HOST:$PORT está disponível, executando comando: $@"
exec "$@"