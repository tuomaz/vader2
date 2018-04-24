var vaderApp = angular.module('vaderApp', [ 'ngRoute', 'nvd3ChartDirectives', 'vaderControllers' ]);

vaderApp.config([ '$routeProvider', function($routeProvider) {
    $routeProvider.when('/', {
        templateUrl : 'resources/partials/start.html',
        controller : 'StartController'
    }).when('/graph/:sensorName', {
        templateUrl : 'resources/partials/graph.html',
        controller : 'GraphController'
    }).
      otherwise({
        redirectTo: '/'
      });
} ]);