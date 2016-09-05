/**
 * Created by ParkJun on 2016-09-05.
 */
var myApp = angular.module('myRegi', []);
myApp.config(function ($httpProvider) {

    // $httpProvider에서 header 를 수정합니다.

    $httpProvider.defaults.useXDomain = true;
    delete $httpProvider.defaults.headers.common['X-Requested-With'];
    //$httpProvider.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded';

});
myApp.controller('SearchCtrl', ['$scope', '$http', function($scope, $http) {
    $scope.search = function() {
        $scope.BT = $scope.searchBT.toString()
        $scope.result = $scope.BT + "의 검색결과\n";
        $scope.ref = "0";
        var reg = /[가-힣a-zA-Z]+#[0-9]{4,5}/;
        if(!reg.test($scope.BT)) {
            $scope.result += "올바르지 않은 배틀태그 형식입니다\n";
            return;
        }
        $scope.url = /*"https://crossorigin.me/https://playoverwatch.com/ko-kr/career/pc/kr/"*/"http://localhost:8080/register";
        $http.post($scope.url, $scope.BT).success(function(data, status) {
            $scope.status = status;
            $scope.data = data;
            /*
            var reg_lv = /<div class="u-vertical-center">([0-9]+)<\/div>/;
            var reg_rank = /<div class="u-align-center h6">([0-9]+)<\/div>/;
            var str_lv = data.toString().match(reg_lv);
            var str_rank = data.toString().match(reg_rank);
            if(str_rank == null) {
                $scope.result += $scope.BT.split('#')[0] +"  Level : " + str_lv[1] + "  Rank : 이 플레이어는 경쟁전을 하지 않았습니다.";
            }
            else {
                $scope.result += $scope.BT.split('#')[0] +"  Level : " + str_lv[1] + "  Rank : "+str_rank[1];
                //이 경우에 대해서 등록절차를 밟자...
                $scope.ref = "1"; //등록 버튼 생성

            }*/
        }).error(function(data, status) {
            $scope.data = data || "Request Failed";
            $scope.status = status;
            $scope.result += "없는 계정명이거나 네트워크상의 문제로 검색이 지연중입니다.";
        });
    }
}]);
