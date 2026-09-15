# Kubernetes Pod Trace Lab

Tomcat 8에서 동작하는 Kubernetes 교육용 웹 애플리케이션입니다.

## 화면에 표시되는 정보
- 접속 도메인: 실제 요청의 `X-Forwarded-Host` 또는 `Host` 헤더
- Namespace: Kubernetes Downward API
- Pod Name: Kubernetes Downward API
- Pod IP: Kubernetes Downward API

ServiceAccount, Role, RoleBinding, Kubernetes API 조회는 사용하지 않습니다.

## 저장 기능
웹의 저장 버튼을 누르면 아래 정보가 `/mnt/k8s-training-info.txt`에 저장됩니다.
- Domain
- Namespace
- Pod Name
- Pod IP

## 이미지 빌드
```bash
docker build -t YOUR_REGISTRY/k8s-training-web:1.0.0 .
docker push YOUR_REGISTRY/k8s-training-web:1.0.0
```

## Kubernetes 배포
`k8s/00-all-in-one.yaml`에서 아래 항목만 환경에 맞게 수정합니다.
1. PVC의 `storageClassName`
2. Deployment의 `image`
3. Ingress의 `ingressClassName`
4. Ingress의 `host`

```bash
kubectl apply -f k8s/00-all-in-one.yaml
```

## 실습 포인트
Replica를 늘린 뒤 브라우저를 여러 번 새로고침하면 어떤 Pod가 응답했는지 Pod Name과 Pod IP로 확인할 수 있습니다.

저장 버튼을 누른 후 아래처럼 확인할 수 있습니다.
```bash
kubectl exec deploy/training-web -- cat /mnt/k8s-training-info.txt
```

Pod를 재생성한 뒤에도 파일이 남아 있으면 PVC의 영속성을 확인할 수 있습니다.
