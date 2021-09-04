### zio-akka-http-tapir

#### Sample project that contains examples for:
* [Akka Http](https://doc.akka.io/docs/akka-http/current/introduction.html) server implemented as [ZIO Layer](https://zio.dev/docs/datatypes/contextual/zlayer)
* HTTP endpoints implemented with [Tapir](https://tapir.softwaremill.com/en/v0.18.3/) 
* Model implementation using [Newtype](https://zio.github.io/zio-prelude/docs/newtypes/) from [ZIO Prelude](https://zio.github.io/zio-prelude/)

#### Usage:
Run `Main`. Server will start at `localhost:8080`.

```json5
// POST localhost:8080/user
{
  "name": "Jon Doe",
  "role": {
    "type": "Administrator",
    "securityCode": 256
  }
}
```
Dummy endpoint will return passed object.

Go to `localhost:8080/swagger` to take a look at the documentation generated with [Tapir](https://tapir.softwaremill.com/en/v0.18.3/)
