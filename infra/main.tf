resource "null_resource" "minikube" {

  provisioner "local-exec" {

  command = "minikube start --driver=docker --cpus=2 --memory=6100"

  }
}

resource "null_resource" "wait_cluster" {

  depends_on = [
    null_resource.minikube
  ]

  provisioner "local-exec" {

      command = "kubectl wait --for=condition=Ready nodes --all --timeout=300s"

  }
}

resource "kubernetes_namespace" "oficina" {

  depends_on = [
    null_resource.wait_cluster
  ]

  metadata {
    name = var.namespace_app
  }
}

resource "kubernetes_namespace" "argocd" {

  depends_on = [
    null_resource.wait_cluster
  ]

  metadata {
    name = var.namespace_argocd
  }
}

resource "helm_release" "metrics_server" {
  depends_on = [
    kubernetes_namespace.oficina
  ]

  name       = "metrics-server"
  namespace  = "kube-system"

  repository = "https://kubernetes-sigs.github.io/metrics-server"
  chart      = "metrics-server"

  set = [
    {
      name  = "args[0]"
      value = "--kubelet-insecure-tls"
    }
  ]
}


resource "helm_release" "ingress_nginx" {

  depends_on = [
    kubernetes_namespace.oficina
  ]

  name = "ingress-nginx"

  repository = "https://kubernetes.github.io/ingress-nginx"

  chart = "ingress-nginx"

  namespace = "ingress-nginx"

  create_namespace = true

  timeout = 600

  set = [
    {
      name  = "controller.service.type"
      value = "NodePort"
    }
  ]
}

resource "helm_release" "argocd" {

  depends_on = [
    kubernetes_namespace.argocd
  ]

  name = "argocd"

  repository = "https://argoproj.github.io/argo-helm"

  chart = "argo-cd"

  namespace = var.namespace_argocd
}