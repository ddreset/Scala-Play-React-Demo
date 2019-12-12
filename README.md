[![MIT License][license-badge]][LICENSE]

>This is a demo for using Scala, Play! and React. It is developed based on [Scala Play React Seed project](https://github.com/yohangz/scala-play-react-seed)

# Backend API

GET http://localhost:9000/api/club list all clubs and their members

POST http://localhost:9000/api/club insert new club and its members

(API from Scala Play React Seed project:

GET http://localhost:9000/

GET http://localhost:9000/api/summary

GET http://localhost:9000/*file)

# Database

[H2 in memory database](https://www.playframework.com/documentation/2.7.x/Developing-with-the-H2-Database)

[Play-Slick](https://github.com/playframework/play-slick)

**everytime start sbt environment, you have to manually apply evolutions script again: just open [backend endpoint](http://localhost:9000)**

# Frontend

List clubs and their members: http://localhost:3000/

Insert new club and its members: http://localhost:3000/newClub

(example page from Scala Play React Seed project: http://localhost:3000/app)

## License

This software is licensed under the MIT license

[license-badge]: http://img.shields.io/badge/license-MIT-blue.svg?style=flat
[license]: https://github.com/yohangz/java-play-react-seed/blob/master/README.md

[yohan-profile]: https://github.com/yohangz
[lahiru-profile]: https://github.com/lahiruz
[gayan-profile]: https://github.com/Arty26
