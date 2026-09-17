# K-Int scoped discovery candidate

Foundry backlog 000088. `1.0.55-kint.1` is published. Protected foundry-k8s pipeline
`2856717726` passed 67 unit tests and seven Envoy integration tests, then job
`16553518712` published and verified parent/cache/server artifacts. Consumer-group
readback also matches the retained qualification bundle.

- Upstream PR477 head: `7cd97fc5661a8d54f31792cf6dbb8a465c9d74ff`.
- Portable compatibility repair: `0cd2e96` (separate from this packaging change).
- Exact release source: `69d7f1c504885bd46be1c9d0b385f7637f9683b6`.
- Parent/cache/server release version: `1.0.55-kint.1`.
- Generated Envoy API dependency: upstream `io.envoyproxy.controlplane:api:1.0.55`.
- License: upstream Apache-2.0, preserved in `LICENSE`.

Main preserves upstream's subsequent development history/version; it is not the
source coordinate for the immutable K-Int release. Check out the exact release
commit to reproduce its artifacts. Main cache/server production sources match
that release; main's updated test dependencies and Envoy fixture have also passed
all 74 tests. Generated API remains the published `1.0.55` dependency.

Build the parent, cache and server with Maven 3.9.9 and JDK17:

```sh
mvn -B -ntp -Dmaven.repo.local=/absolute/empty/local-repository \
  -Dmaven.javadoc.skip=true -pl .,cache,server install
```

`install` writes only the specified local repository. Tests require a Docker API;
rootless Podman works with `DOCKER_HOST` set to its socket. Where Ryuk is disabled,
verify test-container cleanup. Use a scrubbed process environment.

The generated API project is intentionally excluded; consumers use the exact
upstream published API artifact. Do not deploy this reactor using its inherited
upstream publication settings. Remote publication requires the separately
reviewed K-Int CI destination and credentials. The protected foundry-k8s job
uploads parent/cache/server through Maven Deploy Plugin to
`https://maven.k-int.com/repository/maven-releases` (hosted); `/repository/releases`
is the read-only consumption group. Use the manifest-pinned commit and manual
publication job; never publish changed bytes under the same release version.

The upstream PR commits and portable repair remain separable from K-Int package
versions so a contribution can target the original PR branch. Nothing has been
submitted upstream by this work.
