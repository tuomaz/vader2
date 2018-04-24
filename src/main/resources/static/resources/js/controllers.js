'use strict';

/* Controllers */

var vaderControllers = angular.module('vaderControllers', ['nvd3ChartDirectives']);

vaderControllers.controller('StartController', [ '$scope', '$http', '$timeout',
        function($scope, $http, $timeout) {
            $scope.loadData = function() {
                $http({
                    method : 'GET',
                    url : 'rest/sample/get',
                    cache : false
                }).success(function(data) {
                    $scope.samples = data;
                });
            };

            $scope.intervalFunction = function() {
                $timeout(function() {
                    $scope.loadData();
                    $scope.intervalFunction();
                }, 1000 * 60 * 4)
            };

            $scope.intervalFunction();
            $scope.loadData();
        } ]);

vaderControllers.controller('GraphController', ['$scope', '$http', '$routeParams',
    function($scope, $http, $routeParams){
        $scope.loadData = function(hours) {
            $http({
                method : 'GET',
                url : 'rest/sample/get/graph/' + $routeParams.sensorName + '/' + hours,
                cache : false
            }).success(function(data) {
                $scope.exampleData = data;
                $scope.sensorName = data[0].realName;
            });
        };
        $scope.loadData(24);

        $scope.xAxisTickFormatFunction = function(){
            return function(d){
                return d3.time.format('%H.%M	')(new Date(d));
            }
        };

        $scope.showMonth = function(){
        	$scope.loadData(24 * 30);
        };
    }]);
