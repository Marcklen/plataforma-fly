#!/bin/sh
# Original: https://github.com/vishnubob/wait-for-it

HOST=$1
PORT=$2
shift 2
CMD="$@"

until nc -z "$HOST" "$PORT"; do
  echo "Aguardando $HOST:$PORT..."
  sleep 2
done

echo "$HOST:$PORT está disponível, executando comando: $CMD"
exec $CMD