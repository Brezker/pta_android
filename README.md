# Public Transport Android App

## Uso en conjunto con Public Transport API (Laravel)

Aplicacion de transporte publico que ofrece al usuario ver el tiempo que tardan en pasar los distintos tranportes por su locacion, ademas de que permite a los 'checadores anotar la informacion de los mismos para su actualizacion en tiempo real.
La API Laravel la pueden encontrar en [public_transport_api](https://github.com/Brezker/public_transport_api).

## Video demostración
https://github.com/Brezker/ryzentosh-5-3500x-rx570-b450/assets/38386181/b7bea1e0-566a-41da-8757-2464afd1fd48

[YouTube Video](https://www.youtube.com/watch?v=X4VouSl4a74&t=26s)

## Installation & updates

`composer create-project codeigniter4/appstarter` then `composer update` whenever
there is a new release of the framework.

## Important Change with index.php

`index.php` is no longer in the root of the project! It has been moved inside the *public* folder,
for better security and separation of components.

**Please** read the user guide for a better explanation of how CI4 works!

## Repository Management

We use GitHub issues, in our main repository, to track **BUGS** and to track approved **DEVELOPMENT** work packages.
We use our [forum](http://forum.codeigniter.com) to provide SUPPORT and to discuss
FEATURE REQUESTS.

## Server Requirements

PHP version 7.4 or higher is required, with the following extensions installed:

- [intl](http://php.net/manual/en/intl.requirements.php)
- [mbstring](http://php.net/manual/en/mbstring.installation.php)

> **Warning**
> The end of life date for PHP 7.4 was November 28, 2022. If you are
> still using PHP 7.4, you should upgrade immediately. The end of life date

