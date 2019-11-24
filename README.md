# CleanByAutoGenerateUseCase
熟悉clean架构的人应该清楚，clean架构需要编写大量的UseCase,并且每个UseCase代码几乎相同，这种工作是沉重繁琐的，本项目即为自动生成UseCase的apt工具，只需要几个注解结合apt自动生成代码，即可帮你从繁琐的工作中解放出来
# 项目结构
usecase  提供UseCase基类以及threadpool，后期将移除这个lib，将基类也使用apt工具自动生成
usecaseannotaion 提供注解的lib
usecaseannotaionapt  apt生成lib
app   demo
# 使用方法
将usecase usecaseannotaion  usecaseannotaionapt三个moudle拷入工程
并在settings.gradle  中添加include  include ':app',':usecaseannotation', ':usecaseannotationapt', ':usecase'
然后在app的build.gradle添加依赖
    implementation project(path: ':usecaseannotation')
    annotationProcessor project(path: ':usecaseannotationapt')
    implementation project(path: ':usecase')
    
 后期将提供maven依赖
 # 教程
```java
@UseCases
public interface TaskUseCases {
    @UseCase
    void get(@UseCaseRepository TaskRepository taskRepository, @RequestParams int id, @ResponseParams Task task);
    @UseCase
    void save(@UseCaseRepository TaskRepository taskRepository, @RequestParams Task task, @ResponseParams Boolean success);
    @UseCase
    void delete(@UseCaseRepository TaskRepository taskRepository, @RequestParams Task task, @ResponseParams Boolean success);
}
```
@UseCases  注解这是一个entity的usecases,在接口上使用，支持自定义名字，用于定义生成类package路径
@UseCase  注解这是一个UseCase,在方法上使用，将会自动生成一个UseCase，支持自定义UseCase操作名，若为默认，则是函数名字
@UseCaseRepository  为UseCase提供 Repository，支持定义获取数据的方法名，若为默认，则是@UseCase注解的函数名字
@RequestParams  @ResponseParams定义UseCase请求和返回参数类型


# 后续功能
目前改apt提供自定义功能较少，后期将持续维护提供各种自定义功能帮你自动生成你所需的业务代码（如自定义基类UseCase  自定义ThreadPool  自定义类型转换  自定义数据请求逻辑）
