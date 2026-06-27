resource "minikube_cluster" "cluster" {

  driver = "docker"

  cpus = 2

  memory = 6100

  container_runtime = "docker"

}


resource "kubernetes_namespace" "oficina" {

  depends_on = [
    minikube_cluster.cluster
  ]

  metadata {
    name = var.namespace_app
  }
}

resource "kubernetes_namespace" "argocd" {

  depends_on = [
    minikube_cluster.cluster
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

locals {

    manifests_path = "${path.module}/../k8s"

}

resource "kubectl_manifest" "configmap" {

    depends_on = [ kubernetes_namespace.oficina ]

    yaml_body = file("${local.manifests_path}/configmap.yaml")

}

resource "kubectl_manifest" "secret" {

    depends_on = [ kubernetes_namespace.oficina ]

    yaml_body = file("${local.manifests_path}/secret.yaml")

}

resource "kubectl_manifest" "pvc" {

    depends_on = [ kubernetes_namespace.oficina ]

    yaml_body = file("${local.manifests_path}/pvc.yaml")

}

resource "kubectl_manifest" "deployment" {

    depends_on = [

        kubectl_manifest.configmap,

        kubectl_manifest.secret,

        kubectl_manifest.pvc

    ]

    yaml_body = file("${local.manifests_path}/deployment.yaml")

}

resource "kubectl_manifest" "service" {

    depends_on = [ kubectl_manifest.deployment ]

    yaml_body = file("${local.manifests_path}/service.yaml")

}

resource "kubectl_manifest" "hpa" {

    depends_on = [

        helm_release.metrics_server,

        kubectl_manifest.deployment

    ]

    yaml_body = file("${local.manifests_path}/hpa.yaml")

}
