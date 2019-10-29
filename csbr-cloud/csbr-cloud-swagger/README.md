# swagger组件
swagger组件，引入了此组件的项目只需要做少量的yml配置及在启动类开启swagger(@EnableSwagger2)即可访问swagger页面

## 核心配置
- SwaggerProperties：swagger配置项定义文件（后期需要新增属性可以添加到此类中）
- SwaggerAutoConfiguration：swagger自动配置类（里面有生成swagger配置，后期添加属性后可以修改此类中方法）

## 属性
```properties
# 标题
csbr.swagger.title=
# 描述
csbr.swagger.description=
# 版本
csbr.swagger.version=
# 联系人
csbr.swagger.contactName=
# 联系url地址
csbr.swagger.contactUrl=
# 联系email
csbr.swagger.contactEmail=
# 许可证
csbr.swagger.license=
# 许可证url
csbr.swagger.licenseUrl=
# 服务条款url
csbr.swagger.termsOfServiceUrl=
# 是否需要鉴权（默认为true）
csbr.swagger.needAuth=
# 需要鉴权的路径正则（在needAuth=true时才会有效）
csbr.swagger.securityPathRegex=
```

