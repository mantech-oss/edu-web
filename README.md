# Kubernetes Traffic Lab

Tomcat 8에서 동작하는 Kubernetes 교육용 웹 애플리케이션입니다.

## 학습 목표
- Ingress로 접속한 실제 Host 확인
- Service → Pod 요청 전달 흐름 확인
- Downward API로 Pod 이름/IP/Namespace/Node 확인
- PVC를 `/volume`에 마운트하고 현재 정보를 텍스트 파일로 저장

## 동작 방식
- Ingress URL/Host: 요청의 `X-Forwarded-Host` 또는 `Host` 헤더
- Service 이름/포트: `SERVICE_NAME`, `SERVICE_PORT` 환경변수
- Pod 이름/IP/Namespace/Node: Kubernetes Downward API 환경변수
- 저장 경로: `VOLUME_PATH` 환경변수, 기본 `/volume`
- 저장 파일: `/volume/k8s-training-info.txt`

## 이미지 빌드
```bash
docker build -t YOUR_REGISTRY/k8s-training-web:1.0.0 .
docker push YOUR_REGISTRY/k8s-training-web:1.0.0
```

## Kubernetes 배포
`k8s/00-all-in-one.yaml`에서 아래 항목을 환경에 맞게 수정합니다.
1. PVC의 `storageClassName`
2. Deployment의 `image`
3. Ingress의 `ingressClassName`
4. Ingress의 `host`

```bash
kubectl apply -f k8s/00-all-in-one.yaml
```

## PV/PVC 실습
웹에서 `/volume에 저장` 버튼을 누른 후 확인:
```bash
kubectl exec deploy/training-web -- cat /volume/k8s-training-info.txt
```

Pod를 재생성한 뒤에도 같은 파일이 남아있는지 확인하면 PVC의 영속성을 쉽게 설명할 수 있습니다.

## Service → Pod 실습
Replica를 여러 개로 늘리면 새로고침 때 어느 Pod가 응답했는지 화면에서 확인할 수 있습니다.
단, `ReadWriteOnce` PVC는 스토리지 특성상 여러 노드의 Pod에서 동시에 마운트되지 않을 수 있습니다. 이 실습은 RWX PVC를 사용하거나, Service 실습 시 잠시 volumeMount를 제외하는 방식이 안전합니다.
