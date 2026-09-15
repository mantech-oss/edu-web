# Kubernetes Pod Trace Lab

Tomcat 8에서 동작하는 Kubernetes 교육용 웹 애플리케이션입니다.

## 화면에 표시되는 정보
- 접속 도메인: 실제 요청의 `X-Forwarded-Host` 또는 `Host` 헤더
- Pod Name: 컨테이너 내부 `hostname` 명령 결과
- Pod IP: 컨테이너 내부 `hostname -I` 명령 결과

Namespace, Downward API, ServiceAccount, Role, RoleBinding, Kubernetes API 조회는 사용하지 않습니다.

## 저장 기능
웹의 저장 버튼을 누르면 아래 정보가 `/mnt/k8s-training-info.txt`에 저장됩니다.
- Domain
- Pod Name
- Pod IP

## 이미지 빌드
```bash
docker build -t YOUR_REGISTRY/k8s-training-web:1.0.0 .
docker push YOUR_REGISTRY/k8s-training-web:1.0.0
```

## Kubernetes 배포
`k8s/00-all-in-one.yaml`에서 환경에 맞게 PVC StorageClass, 이미지, IngressClass, Host를 수정합니다.

```bash
kubectl apply -f k8s/00-all-in-one.yaml
```

## 실습 포인트
Replica를 늘린 뒤 브라우저를 여러 번 새로고침하면 Pod Name과 Pod IP가 바뀌는지 확인할 수 있습니다.

저장 내용 확인:
```bash
kubectl exec deploy/training-web -- cat /mnt/k8s-training-info.txt
```

Pod를 재생성한 뒤에도 파일이 남아 있으면 PVC의 영속성을 확인할 수 있습니다.
