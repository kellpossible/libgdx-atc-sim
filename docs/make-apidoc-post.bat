pushd ..\
./gradlew -info alljavadoc
popd

xcopy ..\src\Libgdx_ATC_Simulator\build\docs\javadoc\ _build\html\Libgdx_ATC_Simulator\Design_and_API\javadoc\ /e /i /h