package io.envoyproxy.controlplane.cache.v3;

import static io.envoyproxy.controlplane.cache.Resources.TYPE_URLS_TO_RESOURCE_TYPE;

import com.google.auto.value.AutoValue;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.protobuf.Message;
import io.envoyproxy.controlplane.cache.Resources;
import io.envoyproxy.controlplane.cache.Resources.ResourceType;
import io.envoyproxy.controlplane.cache.SnapshotConsistencyException;
import io.envoyproxy.controlplane.cache.SnapshotResources;
import io.envoyproxy.controlplane.cache.VersionedResource;
import io.envoyproxy.envoy.config.cluster.v3.Cluster;
import io.envoyproxy.envoy.config.core.v3.TypedExtensionConfig;
import io.envoyproxy.envoy.config.endpoint.v3.ClusterLoadAssignment;
import io.envoyproxy.envoy.config.listener.v3.Listener;
import io.envoyproxy.envoy.config.route.v3.RouteConfiguration;
import io.envoyproxy.envoy.config.route.v3.ScopedRouteConfiguration;
import io.envoyproxy.envoy.extensions.transport_sockets.tls.v3.Secret;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * {@code Snapshot} is a data class that contains an internally consistent snapshot of v3 xDS resources. Snapshots
 * should have distinct versions per node group.
 */
@AutoValue
public abstract class Snapshot extends io.envoyproxy.controlplane.cache.Snapshot {

  /** Creates a snapshot with a common version. */
  public static Snapshot create(
      Iterable<Cluster> clusters,
      Iterable<ClusterLoadAssignment> endpoints,
      Iterable<Listener> listeners,
      Iterable<RouteConfiguration> routes,
      Iterable<Secret> secrets,
      String version) {
    return createWithScopedRoutes(
        clusters,
        endpoints,
        listeners,
        routes,
        Collections.emptySet(),
        secrets,
        Collections.emptySet(),
        version);
  }

  /** Creates a snapshot with a common version. */
  public static Snapshot create(
      Iterable<Cluster> clusters,
      Iterable<ClusterLoadAssignment> endpoints,
      Iterable<Listener> listeners,
      Iterable<RouteConfiguration> routes,
      Iterable<Secret> secrets,
      Iterable<TypedExtensionConfig> extensionConfigs,
      String version) {
    return createWithScopedRoutes(
        clusters,
        endpoints,
        listeners,
        routes,
        Collections.emptySet(),
        secrets,
        extensionConfigs,
        version);
  }

  /** Creates a snapshot with independent resource-type versions. */
  public static Snapshot create(
      Iterable<Cluster> clusters,
      String clustersVersion,
      Iterable<ClusterLoadAssignment> endpoints,
      String endpointsVersion,
      Iterable<Listener> listeners,
      String listenersVersion,
      Iterable<RouteConfiguration> routes,
      String routesVersion,
      Iterable<Secret> secrets,
      String secretsVersion) {
    return createWithScopedRoutes(
        clusters,
        clustersVersion,
        endpoints,
        endpointsVersion,
        listeners,
        listenersVersion,
        routes,
        routesVersion,
        Collections.emptySet(),
        "",
        secrets,
        secretsVersion,
        Collections.emptySet(),
        "");
  }

  /** Creates a snapshot with independent resource-type versions. */
  public static Snapshot create(
      Iterable<Cluster> clusters,
      String clustersVersion,
      Iterable<ClusterLoadAssignment> endpoints,
      String endpointsVersion,
      Iterable<Listener> listeners,
      String listenersVersion,
      Iterable<RouteConfiguration> routes,
      String routesVersion,
      Iterable<Secret> secrets,
      String secretsVersion,
      Iterable<TypedExtensionConfig> extensionConfigs,
      String extensionConfigsVersion) {
    return createWithScopedRoutes(
        clusters,
        clustersVersion,
        endpoints,
        endpointsVersion,
        listeners,
        listenersVersion,
        routes,
        routesVersion,
        Collections.emptySet(),
        "",
        secrets,
        secretsVersion,
        extensionConfigs,
        extensionConfigsVersion);
  }

  /** Creates a snapshot with a common version. */
  public static Snapshot createWithScopedRoutes(
      Iterable<Cluster> clusters,
      Iterable<ClusterLoadAssignment> endpoints,
      Iterable<Listener> listeners,
      Iterable<RouteConfiguration> routes,
      Iterable<ScopedRouteConfiguration> scopedRoutes,
      Iterable<Secret> secrets,
      String version) {
    return createWithScopedRoutes(
        clusters,
        endpoints,
        listeners,
        routes,
        scopedRoutes,
        secrets,
        Collections.emptySet(),
        version);
  }

  /** Creates a snapshot with a common version. */
  public static Snapshot createWithScopedRoutes(
      Iterable<Cluster> clusters,
      Iterable<ClusterLoadAssignment> endpoints,
      Iterable<Listener> listeners,
      Iterable<RouteConfiguration> routes,
      Iterable<ScopedRouteConfiguration> scopedRoutes,
      Iterable<Secret> secrets,
      Iterable<TypedExtensionConfig> extensionConfigs,
      String version) {
    return new AutoValue_Snapshot(
        SnapshotResources.create(generateSnapshotResourceIterable(clusters), version),
        SnapshotResources.create(generateSnapshotResourceIterable(endpoints), version),
        SnapshotResources.create(generateSnapshotResourceIterable(listeners), version),
        SnapshotResources.create(generateSnapshotResourceIterable(routes), version),
        SnapshotResources.create(generateSnapshotResourceIterable(scopedRoutes), version),
        SnapshotResources.create(generateSnapshotResourceIterable(secrets), version),
        SnapshotResources.create(generateSnapshotResourceIterable(extensionConfigs), version));
  }

  /** Creates a snapshot with independent resource-type versions. */
  public static Snapshot createWithScopedRoutes(
      Iterable<Cluster> clusters,
      String clustersVersion,
      Iterable<ClusterLoadAssignment> endpoints,
      String endpointsVersion,
      Iterable<Listener> listeners,
      String listenersVersion,
      Iterable<RouteConfiguration> routes,
      String routesVersion,
      Iterable<ScopedRouteConfiguration> scopedRoutes,
      String scopedRoutesVersion,
      Iterable<Secret> secrets,
      String secretsVersion) {
    return createWithScopedRoutes(
        clusters,
        clustersVersion,
        endpoints,
        endpointsVersion,
        listeners,
        listenersVersion,
        routes,
        routesVersion,
        scopedRoutes,
        scopedRoutesVersion,
        secrets,
        secretsVersion,
        Collections.emptySet(),
        "");
  }

  /** Creates a snapshot with independent resource-type versions. */
  public static Snapshot createWithScopedRoutes(
      Iterable<Cluster> clusters,
      String clustersVersion,
      Iterable<ClusterLoadAssignment> endpoints,
      String endpointsVersion,
      Iterable<Listener> listeners,
      String listenersVersion,
      Iterable<RouteConfiguration> routes,
      String routesVersion,
      Iterable<ScopedRouteConfiguration> scopedRoutes,
      String scopedRoutesVersion,
      Iterable<Secret> secrets,
      String secretsVersion,
      Iterable<TypedExtensionConfig> extensionConfigs,
      String extensionConfigsVersion) {
    return new AutoValue_Snapshot(
        SnapshotResources.create(generateSnapshotResourceIterable(clusters), clustersVersion),
        SnapshotResources.create(generateSnapshotResourceIterable(endpoints), endpointsVersion),
        SnapshotResources.create(generateSnapshotResourceIterable(listeners), listenersVersion),
        SnapshotResources.create(generateSnapshotResourceIterable(routes), routesVersion),
        SnapshotResources.create(generateSnapshotResourceIterable(scopedRoutes), scopedRoutesVersion),
        SnapshotResources.create(generateSnapshotResourceIterable(secrets), secretsVersion),
        SnapshotResources.create(generateSnapshotResourceIterable(extensionConfigs), extensionConfigsVersion));
  }

  /**
   * Creates an empty snapshot with the given version.
   *
   * @param version the version of the snapshot resources
   */
  public static Snapshot createEmpty(String version) {
    return create(Collections.emptySet(), Collections.emptySet(),
            Collections.emptySet(), Collections.emptySet(), Collections.emptySet(), version);
  }

  /**
   * Returns all v3 cluster items in the CDS payload.
   */
  public abstract SnapshotResources<Cluster> clusters();

  /**
   * Returns all v3 endpoint items in the EDS payload.
   */
  public abstract SnapshotResources<ClusterLoadAssignment> endpoints();

  /**
   * Returns all listener items in the LDS payload.
   */
  public abstract SnapshotResources<Listener> listeners();

  /**
   * Returns all route items in the RDS payload.
   */
  public abstract SnapshotResources<RouteConfiguration> routes();

  /**
   * Returns all scoped route items in the SRDS payload.
   */
  public abstract SnapshotResources<ScopedRouteConfiguration> scopedRoutes();

  /**
   * Returns all secret items in the SDS payload.
   */
  public abstract SnapshotResources<Secret> secrets();

  /**
   * Returns all extension config items in the ECDS payload.
   */
  public abstract SnapshotResources<TypedExtensionConfig> extensionConfigs();

  /**
   * Asserts that all dependent resources are included in the snapshot. All EDS resources are listed by name in CDS
   * resources, and all RDS resources are listed by name in LDS resources.
   *
   * <p>Note that clusters and listeners are requested without name references, so Envoy will accept the snapshot list
   * of clusters as-is, even if it does not match all references found in xDS.
   *
   * @throws SnapshotConsistencyException if the snapshot is not consistent
   */
  public void ensureConsistent() throws SnapshotConsistencyException {
    Set<String> clusterEndpointRefs =
        Resources.getResourceReferences(clusters().versionedResources().values());

    ensureAllResourceNamesExist(Resources.V3.CLUSTER_TYPE_URL, Resources.V3.ENDPOINT_TYPE_URL,
        clusterEndpointRefs, endpoints().versionedResources());

    // RDS tables may be referenced by listeners, scoped routes, or both.
    // Validate their union; requiring each source to reference every table rejects valid SRDS snapshots.
    Set<String> routeRefs = ImmutableSet.<String>builder()
        .addAll(Resources.getResourceReferences(listeners().versionedResources().values()))
        .addAll(Resources.getResourceReferences(scopedRoutes().versionedResources().values()))
        .build();
    ensureAllResourceNamesExist(Resources.V3.LISTENER_TYPE_URL, Resources.V3.ROUTE_TYPE_URL,
        routeRefs, routes().versionedResources());
  }

  /**
   * Returns the resources with the given type.
   *
   * @param typeUrl the type URL of the requested resource type
   */
  public Map<String, VersionedResource<?>> resources(String typeUrl) {
    if (Strings.isNullOrEmpty(typeUrl)) {
      return ImmutableMap.of();
    }

    ResourceType resourceType = TYPE_URLS_TO_RESOURCE_TYPE.get(typeUrl);
    if (resourceType == null) {
      return ImmutableMap.of();
    }

    return versionedResources(resourceType);
  }

  /**
   * Returns the resources with the given type.
   *
   * @param resourceType the requested resource type
   */
  public Map<String, ? extends Message> resources(ResourceType resourceType) {
    switch (resourceType) {
      case CLUSTER:
        return (Map) clusters().resources();
      case ENDPOINT:
        return (Map) endpoints().resources();
      case LISTENER:
        return (Map) listeners().resources();
      case ROUTE:
        return (Map) routes().resources();
      case SCOPED_ROUTE:
        return (Map) scopedRoutes().resources();
      case SECRET:
        return (Map) secrets().resources();
      case EXTENSION_CONFIG:
        return (Map) extensionConfigs().resources();
      default:
        return ImmutableMap.of();
    }
  }

  /**
   * Returns the resources with the given type.
   *
   * @param resourceType the requested resource type
   */
  public Map<String, VersionedResource<?>> versionedResources(ResourceType resourceType) {
    switch (resourceType) {
      case CLUSTER:
        return (Map) clusters().versionedResources();
      case ENDPOINT:
        return (Map) endpoints().versionedResources();
      case LISTENER:
        return (Map) listeners().versionedResources();
      case ROUTE:
        return (Map) routes().versionedResources();
      case SCOPED_ROUTE:
        return (Map) scopedRoutes().versionedResources();
      case SECRET:
        return (Map) secrets().versionedResources();
      case EXTENSION_CONFIG:
        return (Map) extensionConfigs().versionedResources();
      default:
        return ImmutableMap.of();
    }
  }

  /**
   * Returns the version in this snapshot for the given resource type.
   *
   * @param typeUrl the type URL of the requested resource type
   */
  public String version(String typeUrl) {
    return version(typeUrl, Collections.emptyList());
  }

  /**
   * Returns the version in this snapshot for the given resource type.
   *
   * @param typeUrl       the type URL of the requested resource type
   * @param resourceNames list of requested resource names, used to calculate a version for the given resources
   */
  public String version(String typeUrl, List<String> resourceNames) {
    if (Strings.isNullOrEmpty(typeUrl)) {
      return "";
    }

    ResourceType resourceType = TYPE_URLS_TO_RESOURCE_TYPE.get(typeUrl);
    if (resourceType == null) {
      return "";
    }
    return version(resourceType, resourceNames);
  }

  public String version(ResourceType resourceType) {
    return version(resourceType, Collections.emptyList());
  }

  /**
   * Returns the version in this snapshot for the given resource type.
   *
   * @param resourceType  the the requested resource type
   * @param resourceNames list of requested resource names, used to calculate a version for the given resources
   */
  @Override
  public String version(ResourceType resourceType, List<String> resourceNames) {
    switch (resourceType) {
      case CLUSTER:
        return clusters().version(resourceNames);
      case ENDPOINT:
        return endpoints().version(resourceNames);
      case LISTENER:
        return listeners().version(resourceNames);
      case ROUTE:
        return routes().version(resourceNames);
      case SCOPED_ROUTE:
        return scopedRoutes().version(resourceNames);
      case SECRET:
        return secrets().version(resourceNames);
      case EXTENSION_CONFIG:
        return extensionConfigs().version(resourceNames);
      default:
        return "";
    }
  }
}
