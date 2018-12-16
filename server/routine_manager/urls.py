from django.conf.urls import url
from django.urls import path
from django.contrib.staticfiles.urls import staticfiles_urlpatterns
from . import views

urlpatterns = [
    path('', views.index, name='index'),
    path('register/', views.register, name='register'),
    path('login/', views.login, name='login'),
    path('add-child/', views.add_child, name='child'),
    path('push-token/', views.push_token, name='token'),
    path('assets/images/', views.add_image, name='image'),
    path('children/', views.get_children, name='children'),
    path('settings/', views.add_settings, name='settings'),
    path('add-routine/', views.add_routine, name='add-routine'),
    path('remove-child/', views.remove_child, name='remove-child'),
    path('add-history/', views.add_history, name='add-history'),
    path('history/', views.get_history, name='get-history'),
    path('add-reward/', views.add_reward, name='add-reward'),
    path('remove-reward/', views.remove_reward, name='remove-reward')
] + staticfiles_urlpatterns()
